package gak.backend.domain.chatgpt.config;

public class ChatGptConfig {
    public static final String AUTHORIZATION="Authorization";
    public static final String BEARER="Bearer ";
    public static final String API_KEY="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";
    public static final String MODEL="text-davinci-003";
    //"gpt-3.5-turbo"
    public static final Integer MAX_TOKEN=3000; //300
    public static final Double TEMPERATURE=0.0;
    public static final Double TOP_P=1.0;
    public static final String MEDIA_TYPE="application/json; charset=UTF-8";
    public static final String URL="https://api.openai.com/v1/completions";
}

