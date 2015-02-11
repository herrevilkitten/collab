CREATE SCHEMA IF NOT EXISTS Collab;
SET SCHEMA Collab;

CREATE TABLE IF NOT EXISTS Provider (
  id   INTEGER IDENTITY,
  name VARCHAR(16) NOT NULL,
  UNIQUE (name)
);

INSERT INTO Provider (name) VALUES
  ('GOOGLE'),
  ('FACEBOOK'),
  ('TWITTER');

--CREATE SEQUENCE IF NOT EXISTS ProviderIdSeq;

CREATE TABLE IF NOT EXISTS User (
  id                  INTEGER IDENTITY,
  display_Name        VARCHAR(128) NOT NULL,
  email               VARCHAR(512) NOT NULL,
  provider_Id         INTEGER REFERENCES Provider (id),
  provider_Account_Id VARCHAR(256) NOT NULL,
  UNIQUE (provider_Id, provider_Account_Id)
);

--CREATE SEQUENCE IF NOT EXISTS UserIdSeq;

CREATE TABLE IF NOT EXISTS Whiteboard (
  id            INTEGER IDENTITY,
  creator       INTEGER                     NOT NULL REFERENCES User (id),
  description   VARCHAR(2048),
  creation_Time TIMESTAMP DEFAULT now()     NOT NULL
);

--CREATE SEQUENCE IF NOT EXISTS WhiteboardIdSeq;

CREATE TABLE IF NOT EXISTS Object_Types (
  id          INTEGER IDENTITY,
  name        VARCHAR(64)             NOT NULL,
  description VARCHAR(512) DEFAULT '' NOT NULL,
  UNIQUE (name)
);

INSERT INTO Object_Types (name) VALUES
  ('PATH'),
  ('LINE'),
  ('RECTANGLE'),
  ('OVAL');

CREATE TABLE IF NOT EXISTS Whiteboard_Object (
  id             INTEGER IDENTITY,
  creator        INTEGER   NOT NULL REFERENCES USER (id),
  timestamp      TIMESTAMP NOT NULL DEFAULT now(),
  type           INTEGER   NOT NULL REFERENCES Object_Types (id),

-- Object properties
  stroke_Color   INTEGER   NOT NULL DEFAULT (0),
  fill_Color     INTEGER   NOT NULL DEFAULT (0xFFFFFF),
  stroke_Opacity INTEGER   NOT NULL DEFAULT (0),
  fill_Opacity   INTEGER   NOT NULL DEFAULT (0),
  stroke_width   INTEGER   NOT NULL DEFAULT (1),
  z_Index        INTEGER   NOT NULL
);

CREATE TABLE IF NOT EXISTS Whiteboard_Path (
  id      INTEGER NOT NULL REFERENCES Whiteboard_Object (id),

  command CHAR(1) NOT NULL,
  x       DOUBLE  NOT NULL,
  y       DOUBLE  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Whiteboard_Line (
  id      INTEGER NOT NULL REFERENCES Whiteboard_Object (id),

  start_X DOUBLE  NOT NULL,
  start_Y DOUBLE  NOT NULL,
  end_X   DOUBLE  NOT NULL,
  end_Y   DOUBLE  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Whiteboard_Rectangle (
  id      INTEGER NOT NULL REFERENCES Whiteboard_Object (id),

  start_X DOUBLE  NOT NULL,
  start_Y DOUBLE  NOT NULL,
  width   DOUBLE  NOT NULL,
  height  DOUBLE  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Whiteboard_Oval (
  id       INTEGER NOT NULL REFERENCES Whiteboard_Object (id),

  center_X DOUBLE  NOT NULL,
  center_Y DOUBLE  NOT NULL,
  radius_X DOUBLE  NOT NULL,
  radius_Y DOUBLE  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Whiteboard_Users (
  whiteboard INTEGER REFERENCES Whiteboard (id),
  user       INTEGER REFERENCES User (id),
  UNIQUE (whiteboard, user)
);

CREATE TABLE IF NOT EXISTS Action_Types (
  name        VARCHAR(16)             NOT NULL,
  description VARCHAR(512) DEFAULT '' NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS Action (
  id        INTEGER IDENTITY,
  board     INTEGER                 NOT NULL REFERENCES Whiteboard (id),
  actor     INTEGER                 NOT NULL REFERENCES User (id),
  timestamp TIMESTAMP DEFAULT now() NOT NULL,
  type      VARCHAR(12)             NOT NULL REFERENCES Action_Types (name),
  arguments VARCHAR(4000)           NOT NULL DEFAULT ''
);

