CREATE TABLE tbl_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) DEFAULT 'USER',
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tbl_story (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pre_content VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    upvote BIGINT[],
    toc jsonb,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);

CREATE TABLE tbl_comment (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    user_id INTEGER,
    story_id INTEGER,
    parent_comment_id INTEGER,
    FOREIGN KEY (story_id) REFERENCES tbl_story(id),
    FOREIGN KEY (user_id) REFERENCES tbl_user(id)
);
