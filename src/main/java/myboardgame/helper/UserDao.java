package myboardgame.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class handles the operations with the players data.
 */
public class UserDao {

    File file;

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    public List<User> getUsers() {
        var ref = new TypeReference<List<User>>() {
        };
        String path = System.getProperty("user.home")+"/.dao";
        File directory = new File(path);
        file = new File(directory.getPath()+"/users.json");
        try {
           Files.createDirectories(Paths.get(path));
        } catch (IOException io) {
            Logger.error(io.getMessage());
        }
        if (file.exists()) {
                try {
                    return objectMapper.readValue(file, ref);
                } catch (Exception e) {
                    Logger.error("Can not read file "+e.getMessage());
                }
            }
            else {
                try {
                    file.createNewFile();
                    FileWriter writer = new FileWriter(file);
                    writer.write("[ ]");
                    writer.close();
                }catch (IOException io){
                    Logger.error(io.getMessage());
                }
            }

        return new ArrayList<>();
    }

    /**
     * Saves a player name to the Leaderboard.
     * @param userToSave is the player who won the latest match
     */
    public void saveUser(User userToSave) {
        try {
            List<User> users = getUsers();
            users.add(userToSave);
            FileWriter writer = new FileWriter(file);

            objectMapper.writeValue(writer, users);

            Logger.info("Writing file...");
        } catch (Exception e2) {
            Logger.error("Exception caught "+e2.getMessage());
        }
    }

    /**
     * Resets the Leaderboard with an empty {@code list}.
     */
    public void resetUsers() {

        try {
            List<User> users = new ArrayList<>();

            FileWriter writer = new FileWriter(file);

            objectMapper.writeValue(writer, users);
            Logger.info("Writing file...");
            Logger.debug("Reseting Leaderboard");
        } catch (Exception e2) {
            Logger.error("Exception caught "+e2.getMessage());
        }
    }

    /**
     * Returns the top players sorted by the total steps made.
     * @return the top players sorted by the total steps made
     */
    public List<User> getTopUsers() {
        return getUsers().stream().sorted(Comparator.comparing(User::getTotalSteps)).collect(Collectors.toList());
    }
}
