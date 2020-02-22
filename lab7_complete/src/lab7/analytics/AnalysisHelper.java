/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lab7.entities.Comment;
import lab7.entities.Post;
import lab7.entities.User;

/**
 *
 * @author harshalneelkamal
 */
public class AnalysisHelper {

    // find user with Most Likes
    //  key: UserId ; Value: sum of likes from all comments
    public void userWithMostLikes() {
        Map<Integer, Integer> userLikesCount = new HashMap<>();
        Map<Integer, User> users = DataStore.getInstance().getUsers();

        for (User user : users.values()) {
            for (Comment c : user.getComments()) {
                int likes = 0;
                if (userLikesCount.containsKey(user.getId())) {
                    likes = userLikesCount.get(user.getId());
                }
                likes += c.getLikes();
                userLikesCount.put(user.getId(), likes);
            }
        }
        int max = 0;
        int maxId = 0;
        for (int id : userLikesCount.keySet()) {
            if (userLikesCount.get(id) > max) {
                max = userLikesCount.get(id);
                maxId = id;
            }
        }
        System.out.println("User with most likes: " + max + "\n"
                + users.get(maxId));
    }

    // find 5 comments which have the most likes
    public void getFiveMostLikedComment() {
        Map<Integer, Comment> comments = DataStore.getInstance().getComments();
        List<Comment> commentList = new ArrayList<>(comments.values());

        Collections.sort(commentList, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o2.getLikes() - o1.getLikes();
            }
        });

        System.out.println("5 most likes comments: ");
        for (int i = 0; i < commentList.size() && i < 5; i++) {
            System.out.println(commentList.get(i));
        }
    }

    //find post with most comments
    public void getPostWithMostComments() {
        Map<Integer, Post> posts = DataStore.getInstance().getPosts();
        Map<Integer, Integer> mostComments = new HashMap<>();

        for (Post post : posts.values()) {
            for (Comment c : post.getComments()) {
                int comments = 0;
                if (mostComments.containsKey(c.getPostId())) {
                    comments = mostComments.get(c.getPostId());
                }
                comments += c.getPostId();
                mostComments.put(c.getPostId(), comments);
            }
        }
        int max = 0;
        int maxId = 0;
        for (int id : mostComments.keySet()) {
            if (mostComments.get(id) >= max) {
                max = mostComments.get(id);
                maxId = id;
            }
        }
        System.out.println("\nPost id " + maxId + " has most comments.");
        System.out.println("Comments of post id: " + maxId + " " + "are" + posts.get(maxId).getComments() + "\n");
    }

    //Top 5 inactive users based on total posts numbers.
    public void getInactiveUsersBasedOnPosts() {
        Map<Integer, Post> posts = DataStore.getInstance().getPosts();
        Map<Integer, User> users = DataStore.getInstance().getUsers();
        Map<Integer, User> inactiveUsers = new HashMap<>();

        for (User u : users.values()) {
            int count = 0;
            for (Post p : posts.values()) {
                if (p.getUserId() == u.getId()) {
                    count++;
                }
            }
            inactiveUsers.put(count, u);
        }
        System.out.println("5 Inactive Users Based on Posts");
        Set set = inactiveUsers.entrySet();
        Iterator it = set.iterator();
        int i = 0;
        while (it.hasNext() && i < 5) {
            Map.Entry me = (Map.Entry) it.next();
            System.out.println(me.getValue());
            i++;
        }

    }

    // inactive users based on total comments they created
    public void getInactiveUsersWithTotalComments() {
        Map<Integer, Integer> userComments = new HashMap<>();
        Map<Integer, User> userMap = DataStore.getInstance().getUsers();
        ArrayList<Map.Entry<Integer, Integer>> userList = new ArrayList<>();
        for (User user : userMap.values()) {
            userComments.put(user.getId(), user.getComments().size());
        }

        userList = sortArrayList(userComments);
        System.out.println("5 inactive commenting users: ");
        for (int i = 0; i < userList.size() && i < 5; i++) {
            System.out.println("User Id:" + userList.get(i).getKey() + " with Comments:" + userList.get(i).getValue());
        }
    }

    public ArrayList<Map.Entry<Integer, Integer>> sortArrayList(Map<Integer, Integer> usercomments) {
        ArrayList<Map.Entry<Integer, Integer>> userList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> user : usercomments.entrySet()) {
            userList.add(user);
        }
        Comparator<Map.Entry<Integer, Integer>> comparator = new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                Integer user1 = o1.getValue();
                Integer user2 = o2.getValue();
                return user1.compareTo(user2);
            }
        };

        Collections.sort(userList, comparator);
        return userList;
    }

}
