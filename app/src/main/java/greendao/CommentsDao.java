package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yz.books.db.Comments;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "bas_comment".
*/
public class CommentsDao extends AbstractDao<Comments, Long> {

    public static final String TABLENAME = "bas_comment";

    /**
     * Properties of entity Comments.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "id");
        public final static Property ResourceId = new Property(1, int.class, "resourceId", false, "resourceId");
        public final static Property ResourceType = new Property(2, int.class, "resourceType", false, "resourceType");
        public final static Property UserId = new Property(3, int.class, "userId", false, "userId");
        public final static Property UserName = new Property(4, String.class, "userName", false, "userName");
        public final static Property Content = new Property(5, String.class, "content", false, "content");
        public final static Property AddTime = new Property(6, String.class, "addTime", false, "addTime");
        public final static Property ParentId = new Property(7, int.class, "parentId", false, "parentId");
        public final static Property Images = new Property(8, String.class, "images", false, "images");
        public final static Property Sort = new Property(9, int.class, "sort", false, "sort");
        public final static Property Status = new Property(10, String.class, "status", false, "status");
    }


    public CommentsDao(DaoConfig config) {
        super(config);
    }
    
    public CommentsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Comments entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getResourceId());
        stmt.bindLong(3, entity.getResourceType());
        stmt.bindLong(4, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(5, userName);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(7, addTime);
        }
        stmt.bindLong(8, entity.getParentId());
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(9, images);
        }
        stmt.bindLong(10, entity.getSort());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(11, status);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Comments entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getResourceId());
        stmt.bindLong(3, entity.getResourceType());
        stmt.bindLong(4, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(5, userName);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(6, content);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(7, addTime);
        }
        stmt.bindLong(8, entity.getParentId());
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(9, images);
        }
        stmt.bindLong(10, entity.getSort());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(11, status);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Comments readEntity(Cursor cursor, int offset) {
        Comments entity = new Comments( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getInt(offset + 1), // resourceId
            cursor.getInt(offset + 2), // resourceType
            cursor.getInt(offset + 3), // userId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // content
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // addTime
            cursor.getInt(offset + 7), // parentId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // images
            cursor.getInt(offset + 9), // sort
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Comments entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setResourceId(cursor.getInt(offset + 1));
        entity.setResourceType(cursor.getInt(offset + 2));
        entity.setUserId(cursor.getInt(offset + 3));
        entity.setUserName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContent(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAddTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setParentId(cursor.getInt(offset + 7));
        entity.setImages(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSort(cursor.getInt(offset + 9));
        entity.setStatus(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Comments entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Comments entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Comments entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
