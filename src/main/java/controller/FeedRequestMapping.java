package controller;

import java.util.HashMap;
import java.util.Map;

import controller.feed.*;

public class FeedRequestMapping {

    private Map<String, Controller> mappings = new HashMap<String, Controller>();

    public void initMapping() {
        mappings.put("/app/feed", new FeedController());
        mappings.put("/app/feed/write", new WriteController());
        mappings.put("/app/feed/apply", new ApplyController());
        mappings.put("/app/feed/update", new UpdateController());
        mappings.put("/app/feed/delete", new DeleteController());
    }

    public Controller findController(String uri) {
        return mappings.get(uri);
    }
}