package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.CommentTable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores all information about a Comment.
 * @author Elliot Essman
 */
public class Comment {
    
    private User user;
    
    private FileMetadata file;
    
    private int num;
    
    private Comment(User user, FileMetadata file, int num){
        this.user = user;
        this.file = file;
        this.num = num;
    }
    
    public static Comment Create(Connection conn, User user, FileMetadata file) throws ServiceException{
        int num = CommentTable.addComment(conn, user.getUid(), file.getFid());
        return new Comment(user, file, num);
    }
    
    public User getUser(){
        return this.user;
    }
    
    public FileMetadata getFile(){
        return this.file;
    }
    
    public int getNum(){
        return this.num;
    }
}