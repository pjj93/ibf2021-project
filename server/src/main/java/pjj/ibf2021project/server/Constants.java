package pjj.ibf2021project.server;

public class Constants {
    public static final String ENV_TWITTER_BEARER_TOKEN = System.getenv("TWITTER_BEARER_TOKEN");
    public static final String URL_TWITTER_STREAM = "https://api.twitter.com/2/tweets/search/stream";
    public static final String URL_TWITTER_STREAM_RULES = "https://api.twitter.com/2/tweets/search/stream/rules";
    public static final String URL_TWITTER_GET_USER = "https://api.twitter.com/2/users/by/username/";
}
