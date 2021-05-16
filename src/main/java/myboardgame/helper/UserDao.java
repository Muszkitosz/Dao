package myboardgame.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.tinylog.Logger;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public List<User> getUsers() {
        var ref = new TypeReference<List<User>>() {
        };

        try {
            return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("users.json"), ref);
        } catch (Exception e) {
            Logger.error("Can not read file "+e.getMessage());
        }
        return new ArrayList<>();
    }

    public void saveUser(User userToSave) {

        try {
            List<User> users = getUsers();

            users.add(userToSave);

            FileWriter writer = new FileWriter(getClass().getClassLoader().getResource("users.json").getPath());

            objectMapper.writeValue(writer, users);

            Logger.info("Writing file...");
        } catch (Exception e2) {
            Logger.error("Exception caught "+e2.getMessage());
        }
    }

    public void resetUsers() {

        try {
            List<User> users = new ArrayList<>();

            FileWriter writer = new FileWriter(getClass().getClassLoader().getResource("users.json").getPath());

            objectMapper.writeValue(writer, users);

            Logger.info("Writing file...");
            Logger.debug("Reseting Leaderboard");
        } catch (Exception e2) {
            Logger.error("Exception caught "+e2.getMessage());
        }
    }

    public List<User> getTopUsers() {
        Logger.info("Creating leaderboard...");
        return getUsers().stream().sorted(Comparator.comparing(User::getTotalSteps)).collect(Collectors.toList());
    }
}
