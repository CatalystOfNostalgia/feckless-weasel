/* Favorite Course Table */
CREATE TABLE FavoriteCourse(
       uid INT NOT NULL,
       cid INT NOT NULL,
       PRIMARY KEY(uid,cid),
       FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE,
       FOREIGN KEY (cid) REFERENCES Course(id) ON DELETE CASCADE);