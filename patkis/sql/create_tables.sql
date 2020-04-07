-- NOTE: With SQLite, you should always enable foreign keys at run-time:
-- PRAGMA foreign_keys = ON;

-- For future developments:
-- * include a password field
-- * include a user type field
CREATE TABLE IF NOT EXISTS users (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	login TEXT UNIQUE NOT NULL,
	pass TEXT NOT NULL DEFAULT '',
	created TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS snippets (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	owner_id INTEGER,
	is_public INTEGER NOT NULL DEFAULT 0,
	created TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
	title TEXT,
	description TEXT,
	snippet TEXT,
	FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- For future developments:
-- * include e.g. a colour or other stylish option
-- * include a tag type, e.g. language, problem domain, etc.
-- * set up a trigger to remove unused tags?
CREATE TABLE IF NOT EXISTS tags (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	tag TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tags_snippets (
	tag_id INTEGER,
	snippet_id INTEGER,
	tagger_id INTEGER,
	FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
	FOREIGN KEY (snippet_id) REFERENCES snippets(id) ON DELETE CASCADE,
	FOREIGN KEY (tagger_id) REFERENCES users(id) ON DELETE CASCADE
);
