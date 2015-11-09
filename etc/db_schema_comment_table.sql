/* Comment table */
CREATE TABLE Comment (
    uid INT NOT NULL,
    fid INT NOT NULL,
    num INT NOT NULL,
    PRIMARY KEY(fid, uid),
    FOREIGN KEY (uid) REFERENCES User(uid),
    FOREIGN KEY (fid) REFERENCES FileMetadata(fid)
);