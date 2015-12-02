/* Comment table */
CREATE TABLE Comment (
    uid INT NOT NULL,
    fid INT NOT NULL,
    datetime TIMESTAMP NOT NULL,
    text VARCHAR(5000),
    PRIMARY KEY(fid, uid, datetime),
    FOREIGN KEY (uid) REFERENCES User(uid),
    FOREIGN KEY (fid) REFERENCES FileMetadata(fid)
);