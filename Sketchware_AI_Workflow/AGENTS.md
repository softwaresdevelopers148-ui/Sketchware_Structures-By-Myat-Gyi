# AGENTS.md — Sketchware Project Rules

This file is the project-level instruction for an AI coding agent working on Sketchware Pro projects.

## Rule priority

1. User's current request and explicit constraints
2. This `AGENTS.md`
3. `AI_WORKFLOW.md` and `CONTEXT_MAP.md`
4. Guide/reference documents and examples
5. General implementation preferences

If a guide conflicts with the current user request, follow the higher-priority item and explain the conflict.

## Required context loading

Before making a Sketchware change:

1. Read this file.
2. Read `CONTEXT_MAP.md`.
3. Load only the guide section relevant to the task.
4. Inspect the existing project before editing.
5. State the files/blocks that will change.

Do not paste every HTML/PDF guide into every prompt. Use targeted excerpts.

## Sketchware implementation rules

- Prefer AndroidX-compatible Sketchware Pro components.
- Prefer built-in widgets and Java standard/Android APIs before adding a library.
- Add one external library at a time and build-test before adding another.
- Keep UI, data loading, business logic, and storage logic separate.
- Do not put network calls on the Android main thread.
- Use HTTPS for API, image, website, and file URLs.
- Do not hard-code private keys, passwords, Firebase service credentials, or Supabase service-role keys.
- Preserve the user's original URLs and metadata unless the user explicitly asks to transform them.
- Do not replace a requested native UI with WebView.
- Use `AGENTS.md` rules in the local project; do not assume a remote GitHub repository automatically changes an AI model.

## MG PDF Downloader rules

- Native UI is required; do not use WebView for the main app UI.
- Keep `official_page_url`, `cover_url`, `author_url`, and `category_url` unchanged.
- Paid or unauthorized records must not be downloaded.
- A direct PDF URL must not be guessed from a filename or page slug.
- For MmBookshelf mode, use the official page/form flow and validate HTTPS hosts.
- Do not add batch/all-books downloading unless the user has the necessary rights and the server owner explicitly permits it.
- If the catalog does not provide a reliable free/paid/download permission field, stop and ask for an authorized catalog or allowlist.

## Validation requirements

Before declaring completion:

- Check JSON syntax.
- Check Java imports, package names, and balanced braces.
- Confirm all referenced Sketchware View IDs exist or are clearly listed.
- Confirm permissions and Android SDK assumptions.
- Build/test in Sketchware Pro when an Android build environment is available.
- Report what was tested and what was not tested.
