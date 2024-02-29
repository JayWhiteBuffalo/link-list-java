package com.technews.seedData;
import com.technews.model.Comment;
import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.Vote;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import com.technews.repository.CommentRepository;
@Component
public class SeedDataLoader {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;

    private static final String USER_DATA_FILE = "src/main/java/com/technews/seedData/seedData.csv";
    private static final String COMMENT_DATA_FILE = "src/main/java/com/technews/seedData/commentData.csv";
    private static final String POST_DATA_FILE = "src/main/java/com/technews/seedData/postData.csv";
    private static final String VOTE_DATA_FILE = "src/main/java/com/technews/seedData/VoteData.csv";
    private static boolean isUserDataLoaded = false;
    private static boolean isPostDataLoaded = false;
    private static boolean isCommentDataLoaded = false;
    private static boolean isVoteDataLoaded = false;

    public void loadUserData(){
        if (isUserDataLoaded) {
            return;
        }
        try{
            Reader in = new FileReader(USER_DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listItems = parser.getRecords();

            for (CSVRecord record : listItems) {
                String id = record.get("id");
                String username = record.get("username");
                String email = record.get("email");
                String password = record.get("password");

                // Create a User object and save it to the database or perform other operations as needed
                User user = new User(Integer.parseInt(id), username, email, password);
                System.out.println(user);
                // Save the user to the database or perform other operations as needed
                userRepository.save(user);
            }
            isUserDataLoaded = true;
            System.out.println("================================ USER DATA SEED SUCCESSFUL ======================================");

        } catch (IOException e){
            System.out.println(("Failed to load Seed Data"));
            e.printStackTrace();
        }
    }

    public void loadPostData(){
        if (isPostDataLoaded) {
            return;
        }
        try{
            Reader in = new FileReader(POST_DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listItems = parser.getRecords();

            for (CSVRecord record : listItems) {
                String id = record.get("id");
                String title = record.get("title");
                String postUrl = record.get("postUrl");
                String postText = record.get("postText");
                String voteCount = record.get("voteCount");
                String userId = record.get("userId");

                Post post = new Post(Integer.parseInt(id),title,postUrl,postText,Integer.parseInt(voteCount),Integer.parseInt(userId));
                System.out.println(post);
                postRepository.save(post);
            }
            isPostDataLoaded = true;
            System.out.println("================================ POST DATA SEED SUCCESSFUL ======================================");

        } catch (IOException e){
            System.out.println(("Failed to load Seed Data"));
            e.printStackTrace();
        }
    }

    public void loadCommentData(){
        if (isCommentDataLoaded) {
            return;
        }
        try{
            Reader in = new FileReader(COMMENT_DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listItems = parser.getRecords();

            for (CSVRecord record : listItems) {
                String id = record.get("id");
                String commentText = record.get("commentText");
                String userId = record.get("userId");
                String postId = record.get("postId");

                Comment comment = new Comment(Integer.parseInt(id),commentText,Integer.parseInt(userId),Integer.parseInt(postId));
                System.out.println(comment);
                commentRepository.save(comment);
            }
            isCommentDataLoaded = true;
            System.out.println("================================ COMMENT DATA SEED SUCCESSFUL ======================================");

        } catch (IOException e){
            System.out.println(("Failed to load Seed Data"));
            e.printStackTrace();
        }
    }

    public void loadVoteData(){
        if (isVoteDataLoaded) {
            return;
        }
        try{
            Reader in = new FileReader(VOTE_DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listItems = parser.getRecords();

            for (CSVRecord record : listItems) {
                String id = record.get("id");
                String userId = record.get("userId");
                String postId = record.get("postId");

                Vote vote = new Vote(Integer.parseInt(id),Integer.parseInt(userId),Integer.parseInt(postId));
                System.out.println(vote);
                voteRepository.save(vote);
            }
            isVoteDataLoaded = true;
            System.out.println("================================ VOTE DATA SEED SUCCESSFUL ======================================");

        } catch (IOException e){
            System.out.println(("Failed to load Seed Data"));
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void loadSeedData() {
        loadUserData();
        loadPostData();
        loadCommentData();
        loadVoteData();
    }
}
