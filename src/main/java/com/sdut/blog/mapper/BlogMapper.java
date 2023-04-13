package com.sdut.blog.mapper;

import com.sdut.blog.pojo.dto.Blog;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

/**
 * @author 24699
 */
@Mapper
public interface BlogMapper {

    @Select("select * from blog where (approved=1 or approved=3)")
    ArrayList<Blog> getAllBlogs();

    @Insert("insert into blog (publisher,title,content,approved,publish_time,tags,is_reprint,annotation," +
            "first_author,like_count,view_count,comment_count,reprint_count,collect_count) values (#{publisher}," +
            "#{title},#{content},#{approved},#{publishTime},#{tags},#{isReprint},#{annotation},#{firstAuthor},#{likeCount}," +
            "#{viewCount},#{commentCount},#{reprintCount},#{collectCount})")
    Boolean insertBlog(Blog blog);

    @Delete("delete from blog where id = #{blogId}")
    Boolean deleteBlogById(Integer blogId);

    @Update("update blog set title=#{title},content=#{content},tags=#{tags},publish_time=#{publishTime},approved=#{approved} where id=#{id}")
    Boolean updateBlog(Blog blog);

    @Select("select * from blog where id = #{blogId}")
    Blog selectBlogById(Integer blogId);

    @Select("update blog set reprint_count = reprint_count+1 where id=#{blogId}")
    void addReprintCount(Integer blogId);

    @Select("select * from blog where (approved=1 or approved=3) and publisher=#{userId} and tags=#{tag} " +
            "and publisher=first_author and title like concat('%',#{title},'%')")
    ArrayList<Blog> getMyBlogsByCondition(Integer userId, String tag, String title);

    @Select("select * from blog where publisher=#{userId} and publisher!=first_author")
    ArrayList<Blog> getBlogsByCondition_reprint(Integer userId);

    @Select("select * from blog where (approved=1 or approved=3) and tags=#{tag} and is_reprint=0")
    ArrayList<Blog> getTagBlogs(String tag);

    @Select("select * from blog where id=#{id}")
    Blog getBlogById(Integer blogId);

    @Select("select * from blog where (approved=1 or approved=3) and is_reprint=0 order by like_count DESC limit 7")
    ArrayList<Blog> getHotBlogs();

    @Update("update blog set comment_count=comment_count+1 where id=#{blogId}")
    Boolean addCommentCount(Integer blogId);

    @Update("update blog set view_count=view_count+1 where id=#{blogId}")
    Boolean addViewCount(Integer blogId);

    @Update("update blog set collect_count=collect_count+1 where id=#{blogId}")
    Boolean addCollectCount(Integer blogId);

    @Update("update blog set like_count=like_count+1 where id=#{blogId}")
    Boolean addLikeCount(Integer blogId);

    @Update("update blog set collect_count=collect_count-1 where id=#{blogId}")
    Boolean reduceCollectCount(Integer blogId);

    @Update("update blog set like_count=like_count-1 where id=#{blogId}")
    Boolean reduceLikeCount(Integer blogId);

    @Update("update blog set approved=3 where id = #{blogId}")
    Boolean reportBlog(Integer blogId);

    @Select("select * from blog where title like concat('%',#{title},'%') order by publish_time desc")
    ArrayList<Blog> searchBlog(String keyword);

    @Select("select * from blog where approved=0 and publisher=#{userId} and tags=#{tag} " +
            "and title like concat('%',#{title},'%')")
    ArrayList<Blog> getBlogsByCondition_waitJudge(Integer userId, String tag, String title);

    @Select("select * from blog where approved=3 and  publisher=#{userId} and tags=#{tag} " +
            "and title like concat('%',#{title},'%')")
    ArrayList<Blog> getBlogsByCondition_report(Integer userId, String tag, String title);

    @Select("select * from blog where tags=#{tags} and approved=4 order by publish_time desc")
    ArrayList<Blog> getPublic(String tags);

    @Select("select * from blog where approved=2 and publisher=#{userId} and tags=#{tag} " +
            "and title like concat('%',#{title},'%')")
    ArrayList<Blog> getBlogsByCondition_return(Integer userId, String tag, String title);

    @Select("select * from blog where publisher=#{userId}")
    ArrayList<Blog> getAllMyBlogs(Integer userId);
}
