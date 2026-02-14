#!/usr/bin/env bash
# Pre-commit hook for Claude Code: ensures module CLAUDE.md files are staged
# when source files in that module have changed.
#
# Registered via .claude/settings.json as a PreToolUse hook on the Bash tool.
# Receives JSON on stdin with tool_input.command. Outputs deny JSON if modules
# need /analyse-module run before committing.
#
# Compatible with bash 3.2+ (macOS default).
set -euo pipefail

# ── Helper: extract a JSON string field using python3 ───────────────────
json_get() {
  python3 -c "
import json, sys
data = json.load(sys.stdin)
keys = sys.argv[1].split('.')
val = data
for k in keys:
    if isinstance(val, dict) and k in val:
        val = val[k]
    else:
        sys.exit(0)
print(val if val is not None else '', end='')
" "$1" <<< "$INPUT"
}

# ── Helper: emit deny JSON using python3 ────────────────────────────────
emit_deny() {
  python3 -c "
import json, sys
reason = sys.stdin.read()
print(json.dumps({
    'hookSpecificOutput': {
        'hookEventName': 'PreToolUse',
        'permissionDecision': 'deny',
        'permissionDecisionReason': reason
    }
}))
" <<< "$1"
}

# ── Read stdin JSON ─────────────────────────────────────────────────────
INPUT=$(cat)

# ── Extract the bash command ────────────────────────────────────────────
COMMAND=$(json_get "tool_input.command")
if [ -z "$COMMAND" ]; then
  exit 0
fi

# ── Only act on git commit commands ─────────────────────────────────────
# Matches: git commit, git add && git commit, etc.
# Skips: echo "git commit", git commit-tree
if ! echo "$COMMAND" | grep -qE '(^|&&\s*|;\s*)git\s+commit(\s|$)'; then
  exit 0
fi

# ── Resolve project directory ───────────────────────────────────────────
PROJECT_DIR=$(json_get "cwd")
if [ -z "$PROJECT_DIR" ]; then
  PROJECT_DIR="${CLAUDE_PROJECT_DIR:-$(pwd)}"
fi

# ── Get staged files ───────────────────────────────────────────────────
STAGED_FILES=$(git -C "$PROJECT_DIR" diff --cached --name-only 2>/dev/null || true)
if [ -z "$STAGED_FILES" ]; then
  exit 0
fi

# ── Build module path list from settings.gradle.kts ────────────────────
SETTINGS_FILE="$PROJECT_DIR/settings.gradle.kts"
MODULE_PATHS=""

if [ -f "$SETTINGS_FILE" ]; then
  while IFS= read -r line; do
    # Match include(":feature:home") or include(":app")
    if [[ "$line" =~ include\(\":(([^\"]+))\"\) ]]; then
      gradle_path="${BASH_REMATCH[1]}"
      # Convert feature:home -> feature/home
      file_path=$(echo "$gradle_path" | tr ':' '/')
      MODULE_PATHS="${MODULE_PATHS}${file_path}"$'\n'
    fi
  done < "$SETTINGS_FILE"
fi

# build-logic/convention is an includeBuild, not include — add manually
MODULE_PATHS="${MODULE_PATHS}build-logic/convention"

# ── Collect modules with source changes and modules with CLAUDE.md staged ──
CHANGED_MODULES=""
CLAUDE_MODULES=""

while IFS= read -r file; do
  [ -z "$file" ] && continue

  # Check if this is a module's CLAUDE.md
  while IFS= read -r module_path; do
    [ -z "$module_path" ] && continue
    if [ "$file" = "$module_path/CLAUDE.md" ]; then
      CLAUDE_MODULES="${CLAUDE_MODULES}${module_path}"$'\n'
    fi
  done <<< "$MODULE_PATHS"

  # Only trigger on .kt and .kts files
  case "$file" in
    *.kt|*.kts) ;;
    *) continue ;;
  esac

  # Find the longest-matching module path
  best_match=""
  best_len=0
  while IFS= read -r module_path; do
    [ -z "$module_path" ] && continue
    case "$file" in
      "$module_path"/*)
        len=${#module_path}
        if [ "$len" -gt "$best_len" ]; then
          best_match="$module_path"
          best_len=$len
        fi
        ;;
    esac
  done <<< "$MODULE_PATHS"

  if [ -n "$best_match" ]; then
    # Add only if not already in the list
    if ! echo "$CHANGED_MODULES" | grep -qxF "$best_match"; then
      CHANGED_MODULES="${CHANGED_MODULES}${best_match}"$'\n'
    fi
  fi
done <<< "$STAGED_FILES"

# ── Find modules missing CLAUDE.md in staging ──────────────────────────
MISSING_MODULES=""
while IFS= read -r module_path; do
  [ -z "$module_path" ] && continue
  if ! echo "$CLAUDE_MODULES" | grep -qxF "$module_path"; then
    # Convert to Gradle notation for the deny message
    if [ "$module_path" = "build-logic/convention" ]; then
      gradle_name="build-logic/convention"
    else
      gradle_name=":$(echo "$module_path" | tr '/' ':')"
    fi
    MISSING_MODULES="${MISSING_MODULES}${gradle_name}"$'\n'
  fi
done <<< "$CHANGED_MODULES"

# ── All modules have CLAUDE.md staged — allow commit ───────────────────
# Remove trailing newlines for the check
MISSING_TRIMMED=$(echo "$MISSING_MODULES" | sed '/^$/d')
if [ -z "$MISSING_TRIMMED" ]; then
  exit 0
fi

# ── Deny: list modules that need /analyse-module ───────────────────────
MODULE_LIST=""
while IFS= read -r mod; do
  [ -z "$mod" ] && continue
  MODULE_LIST="${MODULE_LIST}  - /analyse-module ${mod}"$'\n'
done <<< "$MISSING_TRIMMED"

REASON="Module CLAUDE.md files are out of date. These modules have staged source changes but their CLAUDE.md is not staged:

${MODULE_LIST}
Run the analyse-module skill for each module listed above, then stage the updated CLAUDE.md files before committing."

emit_deny "$REASON"
