/* Favorite Files Table */
CREATE TABLE FavoriteFile(
       uid INT NOT NULL,
       fid INT NOT NULL,
       PRIMARY KEY(uid,fid),
       FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE,
       FOREIGN KEY (fid) REFERENCES FileMetadata(fid) ON DELETE CASCADE);