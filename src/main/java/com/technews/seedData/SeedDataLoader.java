package com.technews.seedData;
import com.technews.model.*;
import com.technews.repository.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    private UserAttributeRepository userAttributeRepository;

    private static final String USER_DATA_FILE = "src/main/java/com/technews/seedData/seedData.csv";
    private static final String COMMENT_DATA_FILE = "src/main/java/com/technews/seedData/commentData.csv";
    private static final String POST_DATA_FILE = "src/main/java/com/technews/seedData/postData.csv";
    private static final String VOTE_DATA_FILE = "src/main/java/com/technews/seedData/VoteData.csv";
    private static final String ATTRIBUTE_DATA_FILE = "src/main/java/com/technews/seedData/attributesData.csv";
    private static boolean isUserDataLoaded = false;
    private static boolean isPostDataLoaded = false;
    private static boolean isCommentDataLoaded = false;
    private static boolean isVoteDataLoaded = false;

    private static boolean isAttributesDataLoaded = false;
    @Transactional
    public void loadUserData(){
        if (isUserDataLoaded) {
            return;
        }
        try{
            Reader in = new FileReader(USER_DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> listItems = parser.getRecords();

            // Create an AtomicInteger to track the current index
            AtomicInteger currentIndex = new AtomicInteger(1);

            for (CSVRecord record : listItems) {
                String id = record.get("id");
                String username = record.get("username");
                String email = record.get("email");
                String password = record.get("password");

                // Create a User object and save it to the database or perform other operations as needed
                User user = new User(Integer.parseInt(id), username, email, password);
                Integer userId = user.getId();
                // Call loadAttributeData with the user and userId
                loadAttributeData(user, currentIndex);
            }
            isUserDataLoaded = true;
            isAttributesDataLoaded = true;
            System.out.println("================================ USER DATA SEED SUCCESSFUL ======================================");
            System.out.println("================================ ATTRIBUTE DATA SEED SUCCESSFUL ======================================");

        } catch (IOException e){
            System.out.println(("Failed to load Seed Data"));
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadAttributeData(User user, AtomicInteger currentIndex) throws IOException {
        if (isAttributesDataLoaded) {
            return;
        }
        try (Reader in = new FileReader(ATTRIBUTE_DATA_FILE);
             CSVParser parser = CSVFormat.RFC4180.RFC4180.withFirstRecordAsHeader().parse(in)) {
            int index = currentIndex.get();
            // Get the iterator and skip to the current record
            Iterator<CSVRecord> iterator = parser.iterator();

            for (int i = 0; i < index && iterator.hasNext(); i++) {
                iterator.next();
            }
            // Check if there's a next record
            if (iterator.hasNext()) {
                // Get the next record
                CSVRecord record = iterator.next();
                // Increment the currentIndex to indicate that the next record has been processed
                currentIndex.incrementAndGet();
                UserAttributes userAttributes = new UserAttributes();
                userAttributes.setId(Integer.parseInt(record.get("id")));
                userAttributes.setProfilePicture(record.get("profilePicture"));
                userAttributes.setUserHeadline(record.get("userHeadline"));
                userAttributes.setAboutSection(record.get("aboutSection"));
                userAttributes.setKarma((int) Math.round(Math.random() * 33));
                userAttributes.setCreated_time(new Date());
                userAttributes.setCreated_date(new Date());
                userAttributes.updateLastLogin();

                user.setUserAttributes(userAttributes);
                userAttributes.setUser(user);
                userRepository.save(user);
            }
            } catch(IOException e){
                System.out.println("Failed to load Seed Data");
                e.printStackTrace();
            }
        }



    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void loadSeedData() {
        loadUserData();
        loadPostData();
        loadCommentData();
        loadVoteData();
        System.out.println("@~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SEED RUNNER END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~@");
    }
}
