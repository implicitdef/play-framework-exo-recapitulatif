# --- !Ups


INSERT INTO users (name, password) VALUES
  ("Emmanuel", "123"),
  ("Berta", "123"),
  ("Donald", "123");

# --- !Downs

TRUNCATE TABLE users;