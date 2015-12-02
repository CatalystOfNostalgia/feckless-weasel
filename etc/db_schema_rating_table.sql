/* Rating table */
CREATE TABLE Rating (
    uid INT NOT NULL,
    fid INT NOT NULL,
    rating INT NOT NULL,
    PRIMARY KEY(fid, uid),
    FOREIGN KEY (uid) REFERENCES User(uid),
    FOREIGN KEY (fid) REFERENCES FileMetadata(fid)
);