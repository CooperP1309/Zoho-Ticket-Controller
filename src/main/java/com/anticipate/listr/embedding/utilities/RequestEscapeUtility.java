package com.anticipate.listr.embedding.utilities;

/* spring specific modules */
import org.springframework.stereotype.Component;

/* java modules */
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestEscapeUtility
{
        /*
        *   Escapes a string for safe embedding inside a JSON string literal.
        *
        *   Handles quotes, backslashes, and control characters so that
        *   hand-built JSON bodies (e.g. ticket subjects) stay valid JSON.
        */
        public static String escapeJsonString(String input) 
        {
            if (input == null) 
            {
                return "";
            }

            StringBuilder escaped = new StringBuilder(input.length());
            
            for (int i = 0; i < input.length(); i++) 
            {
                char c = input.charAt(i);
                switch (c) 
                {
                    case '"':
                        escaped.append("\\\"");
                        break;
                    case '\\':
                        escaped.append("\\\\");
                        break;
                    case '\b':
                        escaped.append("\\b");
                        break;
                    case '\f':
                        escaped.append("\\f");
                        break;
                    case '\n':
                        escaped.append("\\n");
                        break;
                    case '\r':
                        escaped.append("\\r");
                        break;
                    case '\t':
                        escaped.append("\\t");
                        break;
                    default:
                        if (c < 0x20) 
                        {
                            escaped.append(String.format("\\u%04x", (int) c));
                        } else 
                        {
                            escaped.append(c);
                        }
                }
            }
            return escaped.toString();
        }
    }