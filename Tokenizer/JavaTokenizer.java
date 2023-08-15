import java.util.*;
import java.io.*;

public class JavaTokenizer {
    public static void main(String[] args) throws IOException {
        try {
            String inputFilename;

            if (args.length == 0) {
                System.out.println("Running with a default file. Please enter the name correctly next time.");
                inputFilename = "/home/shrisharanyan/03_College/EOC/Compiler/Tokenizer/inputFiles/Main.jack";
            } else {
                inputFilename = args[0];
            }

            File inputFile = new File(inputFilename);
            Scanner scanner = new Scanner(inputFile);
            String outputFilename = "outputFiles/"
                    + inputFilename.substring(inputFilename.lastIndexOf("/"), inputFilename.indexOf(".")) + ".xml";
            File outputFile = new File(outputFilename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

            List<String> keywords = List.of("class", "constructor", "function", "field", "method", "static", "char",
                    "var",
                    "int", "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while",
                    "return");

            List<String> symbols = List.of("{", "}", "|", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&",
                    "&lt;", "&gt;", "=", "~");

            bw.write("<tokens>\n");
            String currentString = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replaceAll("\n", "");
                line = line.replaceAll("\t", "");

                if (line.length() > 1 && line.substring(0, 2).equals("//")) {
                    continue;
                } else if (line.length() == 0) {
                    continue;
                }

                if (line.isEmpty()) {
                    continue;
                }

                String trimmedLine = line.replaceAll(" ", "");

                if (trimmedLine.startsWith("*") || trimmedLine.startsWith("/")) {
                    continue;
                }

                line = line.replaceAll("\\?", "ques");

                if (line.contains("\"")) {
                    int startIndex = line.indexOf("\"");
                    int endIndex = line.indexOf("\"", startIndex + 1);
                    currentString = line.substring(startIndex + 1, endIndex);
                    line = line.replaceAll("\"" + currentString + "\"", " \" ");
                }

                line = line.replaceAll("\\[", " \\[ ");
                line = line.replaceAll("\\]", " \\] ");
                line = line.replaceAll("\\{", " \\{ ");
                line = line.replaceAll("\\}", " \\} ");
                line = line.replaceAll("\\(", " \\( ");
                line = line.replaceAll("\\)", " \\) ");
                line = line.replaceAll("\\;", " \\; ");
                line = line.replaceAll("\\<", " \\&lt; ");
                line = line.replaceAll("\\>", " \\&gt; ");
                line = line.replaceAll("\\.", " \\. ");
                line = line.replaceAll("\\,", " \\, ");
                line = line.replaceAll("\\-", " \\- ");

                String[] tokens = line.split("\\s+");

                for (String token : tokens) {
                    if (token != null && !token.isEmpty()) {
                        if (token.contains("//")) {
                            break;
                        }

                        char firstChar = token.charAt(0);

                        if (firstChar >= '0' && firstChar <= '9') {
                            bw.write("<integerConstant> ");
                            bw.write(token);
                            bw.write(" </integerConstant>\n");
                        } else if (symbols.contains(token)) {
                            bw.write("<symbol> ");
                            bw.write(token);
                            bw.write(" </symbol>\n");
                        } else if (keywords.contains(token)) {
                            bw.write("<keyword> ");
                            bw.write(token);
                            bw.write(" </keyword>\n");
                        } else if (token.contains("\"")) {
                            bw.write("<stringConstant> ");
                            currentString = currentString.replaceAll("ques", "\\?");
                            bw.write('"' + currentString + '"');
                            bw.write(" </stringConstant>\n");
                        } else {
                            bw.write("<identifier> ");
                            bw.write(token);
                            bw.write(" </identifier>\n");
                        }
                    }
                }
            }

            bw.write("</tokens>\n");
            scanner.close();
            bw.close();
            System.out.println("Operation Successful");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
