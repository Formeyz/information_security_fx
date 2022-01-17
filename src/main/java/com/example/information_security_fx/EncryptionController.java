package com.example.information_security_fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EncryptionController {

    @FXML
    TextField plainTextTextField;

    @FXML
    TextField encodingTextField;

    @FXML
    TextArea detailedInfTextArea;

    @FXML
    Label detailedInfLabel;

    @FXML
    Label encodingTextLabel;

    @FXML
    Button showHideButton;

    @FXML
    Button decryptionButton;

    @FXML
    public void onEncryptionButtonClick() {
        if (!Objects.equals(plainTextTextField.getText(), "")) {
            String plainText = plainTextTextField.getText();
            String check;
            int add = 6 - plainText.length() % 6;
            if (plainText.length() % 6 == 0) {
                check = "";
            } else {
                check = add + "f";
            }
            StringBuilder sb = new StringBuilder(plainText);
            while (sb.length() % 6 != 0) {
                sb.append(add);
            }
            plainText = sb.toString();
            for (int g = 1; g <= 3; g++) {
                ArrayList<String> arrSixEl = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < plainText.length(); i = i + 6) {
                    for (int j = 0; j < 6; j++) {
                        if (i + j < plainText.length()) {
                            stringBuilder.append(plainText.charAt(i+j));
                        } else break;
                    }
                    arrSixEl.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
                detailedInfTextArea.appendText("Текст, разбитый по 6 символов: " + arrSixEl + "\n");
                String left = null;
                String right = null;
                String encBinText = null;
                StringBuilder buildText = new StringBuilder();
                for (int a = 0; a < arrSixEl.size(); a++) {
                    detailedInfTextArea.appendText("ШИФРОВАНИЕ БЛОКА №" + (a + 1) + "\n");
                    for (int i = 0; i < arrSixEl.get(a).length(); i = i + 3) {
                        for (int j = 0; j < 3; j++) {
                            if (i + j < arrSixEl.get(a).length()) {
                                stringBuilder.append(arrSixEl.get(a).charAt(i + j));
                            } else break;
                        }
                        if (i == 0) {
                            left = stringBuilder.toString();
                        } else {
                            right = stringBuilder.toString();
                        }
                        stringBuilder = new StringBuilder();
                    }
                    detailedInfTextArea.appendText("L: " + left + "\n");
                    detailedInfTextArea.appendText("R: " + right + "\n");
                    detailedInfTextArea.appendText("Перевод в двоичную систему счисления левой и правой частей" + "\n");
                    assert left != null;
                    left = stringToBinary(left);
                    assert right != null;
                    right = stringToBinary(right);
                    detailedInfTextArea.appendText("L: " + left + "\n");
                    detailedInfTextArea.appendText("R: " + right + "\n");
                    detailedInfTextArea.appendText("Применение функции к левой части" + "\n");
                    left = caesar(left, g, true);
                    detailedInfTextArea.appendText("L: " + left + "\n");
                    detailedInfTextArea.appendText("R: " + right + "\n");
                    detailedInfTextArea.appendText("Сложение левой и правой частей по модулю 2 (XOR)" + "\n");
                    StringBuilder stringBuilder1 = new StringBuilder();
                    int numLeft;
                    int numRight;
                    for (int i = 0; i < left.length(); i++) {
                        numLeft = Integer.parseInt(Character.toString(left.charAt(i)));
                        numRight = Integer.parseInt(Character.toString(right.charAt(i)));
                        stringBuilder1.append(numLeft ^ numRight);
                    }
                    left = stringBuilder1.toString();
                    detailedInfTextArea.appendText("left XOR right: " + stringBuilder1 + "\n");
                    encBinText = right + left;
                    detailedInfTextArea.appendText("Зашифрованный текст в двоичном коде: " + encBinText + "\n");
                    detailedInfTextArea.appendText("Перевод бинарного зашифрованного сообщения в обычное зашифрованное" + "\n");
                    StringBuilder buildLetter = new StringBuilder();
                    String letter;
                    for (int i = 0; i < encBinText.length(); i = i + 8) {
                        for (int j = 0; j < 8; j++) {
                            buildLetter.append(encBinText.charAt(i + j));
                        }
                        letter = binaryToString(buildLetter.toString());
                        buildText.append(letter);
                        buildLetter = new StringBuilder();
                    }
                    if (g == 3) {
                        encBinText = buildText + check;
                    } else {
                        encBinText = buildText.toString();
                    }
                }
                detailedInfTextArea.appendText("Зашифрованный текст: " + encBinText + "\n");
                encodingTextLabel.setText("Зашифрованный текст:");
                encodingTextField.setText(encBinText);
                plainText = encBinText;
                decryptionButton.setDisable(false);
            }

        } else {
            detailedInfLabel.setVisible(true);
            detailedInfTextArea.setVisible(true);
            showHideButton.setText("Скрыть подробную информацию");
            detailedInfTextArea.appendText("Поле исходного текста не должно быть пустым!" + "\n");
            decryptionButton.setDisable(true);
        }
    }

    @FXML
    public void onDecryptionButtonClick() {
        String encBinText = encodingTextField.getText();
        int add = 0;
        if (encBinText.charAt(encBinText.length() - 1) == 'f') {
            add = Integer.parseInt(String.valueOf(encBinText.charAt(encBinText.length() - 2)));
            StringBuilder stringBuilder = new StringBuilder(encBinText);
            stringBuilder.delete(encBinText.length() - 2, encBinText.length());
            encBinText = stringBuilder.toString();
        }
        for (int g = 3; g >= 1; g--) {
            detailedInfTextArea.appendText("Разбиение текста на блоки по 6 символов" + "\n");
            ArrayList<String> arrSixEl = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < encBinText.length(); i = i + 6) {
                for (int j = 0; j < 6; j++) {
                    if (i + j < encBinText.length()) {
                        stringBuilder.append(encBinText.charAt(i+j));
                    } else break;
                }
                arrSixEl.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }
            detailedInfTextArea.appendText("Текст, разбитый по 6 символов: " + arrSixEl + "\n");
            detailedInfTextArea.appendText("Разбиение блоков из 6 символов на левую и правую часть" + "\n");
            String left = null;
            String right = null;
            StringBuilder buildText = new StringBuilder();
            for (int a = 0; a < arrSixEl.size(); a++) {
                detailedInfTextArea.appendText("РАСШИФРОВКА БЛОКА №" + (a + 1) + "\n");
                for (int i = 0; i < arrSixEl.get(a).length(); i = i + 3) {
                    for (int j = 0; j < 3; j++) {
                        if (i + j < arrSixEl.get(a).length()) {
                            stringBuilder.append(arrSixEl.get(a).charAt(i + j));
                        } else break;
                    }
                    if (i == 0) {
                        left = stringBuilder.toString();
                    } else {
                        right = stringBuilder.toString();
                    }
                    stringBuilder = new StringBuilder();
                }
                detailedInfTextArea.appendText("L: " + left + "\n");
                detailedInfTextArea.appendText("R: " + right + "\n");
                detailedInfTextArea.appendText("Перевод в двоичную систему счисления левой и правой частей" + "\n");
                assert left != null;
                left = stringToBinary(left);
                assert right != null;
                right = stringToBinary(right);
                detailedInfTextArea.appendText("L: " + left + "\n");
                detailedInfTextArea.appendText("R: " + right + "\n");
                detailedInfTextArea.appendText("Сложение левой и правой частей по модулю 2 (XOR)" + "\n");
                StringBuilder stringBuilder1 = new StringBuilder();
                int numLeft;
                int numRight;
                for (int i = 0; i < left.length(); i++) {
                    numLeft = Integer.parseInt(Character.toString(left.charAt(i)));
                    numRight = Integer.parseInt(Character.toString(right.charAt(i)));
                    stringBuilder1.append(numLeft ^ numRight);
                }
                right = stringBuilder1.toString();
                detailedInfTextArea.appendText("left XOR right: " + stringBuilder1 + "\n");
                detailedInfTextArea.appendText("Применение функции к левой части" + "\n");
                right = caesar(right, g, false);
                detailedInfTextArea.appendText("L: " + left + "\n");
                detailedInfTextArea.appendText("R: " + right + "\n");
                encBinText = right + left;
                detailedInfTextArea.appendText("Зашифрованный текст в двоичном коде: " + encBinText + "\n");
                detailedInfTextArea.appendText("Перевод бинарного расшифрованного сообщения в обычное расшифрованное" + "\n");
                StringBuilder buildLetter = new StringBuilder();
                String letter;
                for (int i = 0; i < encBinText.length(); i = i + 8) {
                    for (int j = 0; j < 8; j++) {
                        buildLetter.append(encBinText.charAt(i + j));
                    }
                    letter = binaryToString(buildLetter.toString());
                    buildText.append(letter);
                    buildLetter = new StringBuilder();
                }
            }
            if (g == 1) {
                buildText.delete(buildText.length() - add, buildText.length());
            }
            detailedInfTextArea.appendText("Расшифрованный текст: " + buildText + "\n");
            encodingTextLabel.setText("Расшифрованный текст:");
            encBinText = buildText.toString();
            encodingTextField.setText(buildText.toString());
        }
        decryptionButton.setDisable(true);
    }

    @FXML
    public void onClearButtonClick() {
        plainTextTextField.setText("");
        encodingTextField.setText("");
        detailedInfTextArea.setText("");
        decryptionButton.setDisable(true);
    }

    @FXML
    public void onShowHideButtonClick() {
        if (detailedInfLabel.isVisible()) {
            detailedInfTextArea.setVisible(false);
            detailedInfLabel.setVisible(false);
            showHideButton.setText("Показать подробную информацию");
        } else {
            detailedInfTextArea.setVisible(true);
            detailedInfLabel.setVisible(true);
            showHideButton.setText("Скрыть подробную информацию");
        }
    }

    public static String stringToBinary(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String bin;
            switch (ch) {
                case 'Ё' -> bin = "00000000";
                case 'ё' -> bin = "00000001";
                case '¿' -> bin = "00000010";
                case 'À' -> bin = "00000011";
                case 'Á' -> bin = "00000100";
                case 'Â' -> bin = "00000101";
                case 'Ã' -> bin = "00000110";
                case 'Ä' -> bin = "00000111";
                case 'Å' -> bin = "00001000";
                case 'Æ' -> bin = "00001001";
                case 'Ç' -> bin = "00001010";
                case 'È' -> bin = "00001011";
                case 'É' -> bin = "00001100";
                case 'Ê' -> bin = "00001101";
                case 'Ì' -> bin = "00001110";
                case 'Í' -> bin = "00001111";
                case 'Î' -> bin = "00010000";
                case 'Ï' -> bin = "00010001";
                case 'Ð' -> bin = "00010010";
                case 'Ñ' -> bin = "00010011";
                case 'Ò' -> bin = "00010100";
                case 'Ó' -> bin = "00010101";
                case 'Ô' -> bin = "00010110";
                case 'Õ' -> bin = "00010111";
                case 'Ö' -> bin = "00011000";
                case '×' -> bin = "00011001";
                case 'Ø' -> bin = "00011010";
                case 'Ù' -> bin = "00011011";
                case 'Ú' -> bin = "00011100";
                case 'Û' -> bin = "00011101";
                case 'Ü' -> bin = "00011110";
                case 'Ý' -> bin = "00011111";
                case ' ' -> bin = "00100000";
                case '!' -> bin = "00100001";
                case '"' -> bin = "00100010";
                case '#' -> bin = "00100011";
                case '$' -> bin = "00100100";
                case '%' -> bin = "00100101";
                case '&' -> bin = "00100110";
                case '\'' -> bin = "00100111";
                case '(' -> bin = "00101000";
                case ')' -> bin = "00101001";
                case '*' -> bin = "00101010";
                case '+' -> bin = "00101011";
                case ',' -> bin = "00101100";
                case '-' -> bin = "00101101";
                case '.' -> bin = "00101110";
                case '/' -> bin = "00101111";
                case '0' -> bin = "00110000";
                case '1' -> bin = "00110001";
                case '2' -> bin = "00110010";
                case '3' -> bin = "00110011";
                case '4' -> bin = "00110100";
                case '5' -> bin = "00110101";
                case '6' -> bin = "00110110";
                case '7' -> bin = "00110111";
                case '8' -> bin = "00111000";
                case '9' -> bin = "00111001";
                case ':' -> bin = "00111010";
                case ';' -> bin = "00111011";
                case '<' -> bin = "00111100";
                case '=' -> bin = "00111101";
                case '>' -> bin = "00111110";
                case '?' -> bin = "00111111";
                case '@' -> bin = "01000000";
                case 'A' -> bin = "01000001";
                case 'B' -> bin = "01000010";
                case 'C' -> bin = "01000011";
                case 'D' -> bin = "01000100";
                case 'E' -> bin = "01000101";
                case 'F' -> bin = "01000110";
                case 'G' -> bin = "01000111";
                case 'H' -> bin = "01001000";
                case 'I' -> bin = "01001001";
                case 'J' -> bin = "01001010";
                case 'K' -> bin = "01001011";
                case 'L' -> bin = "01001100";
                case 'M' -> bin = "01001101";
                case 'N' -> bin = "01001110";
                case 'O' -> bin = "01001111";
                case 'P' -> bin = "01010000";
                case 'Q' -> bin = "01010001";
                case 'R' -> bin = "01010010";
                case 'S' -> bin = "01010011";
                case 'T' -> bin = "01010100";
                case 'U' -> bin = "01010101";
                case 'V' -> bin = "01010110";
                case 'W' -> bin = "01010111";
                case 'X' -> bin = "01011000";
                case 'Y' -> bin = "01011001";
                case 'Z' -> bin = "01011010";
                case '[' -> bin = "01011011";
                case '\\' -> bin = "01011100";
                case ']' -> bin = "01011101";
                case '^' -> bin = "01011110";
                case '_' -> bin = "01011111";
                case '`' -> bin = "01100000";
                case 'a' -> bin = "01100001";
                case 'b' -> bin = "01100010";
                case 'c' -> bin = "01100011";
                case 'd' -> bin = "01100100";
                case 'e' -> bin = "01100101";
                case 'f' -> bin = "01100110";
                case 'g' -> bin = "01100111";
                case 'h' -> bin = "01101000";
                case 'i' -> bin = "01101001";
                case 'j' -> bin = "01101010";
                case 'k' -> bin = "01101011";
                case 'l' -> bin = "01101100";
                case 'm' -> bin = "01101101";
                case 'n' -> bin = "01101110";
                case 'o' -> bin = "01101111";
                case 'p' -> bin = "01110000";
                case 'q' -> bin = "01110001";
                case 'r' -> bin = "01110010";
                case 's' -> bin = "01110011";
                case 't' -> bin = "01110100";
                case 'u' -> bin = "01110101";
                case 'v' -> bin = "01110110";
                case 'w' -> bin = "01110111";
                case 'x' -> bin = "01111000";
                case 'y' -> bin = "01111001";
                case 'z' -> bin = "01111010";
                case '{' -> bin = "01111011";
                case '|' -> bin = "01111100";
                case '}' -> bin = "01111101";
                case '~' -> bin = "01111110";
                case 'Þ' -> bin = "01111111";
                case 'ß' -> bin = "10000000";
                case 'à' -> bin = "10000001";
                case 'á' -> bin = "10000010";
                case 'â' -> bin = "10000011";
                case 'ã' -> bin = "10000100";
                case 'ä' -> bin = "10000101";
                case 'å' -> bin = "10000110";
                case 'æ' -> bin = "10000111";
                case 'ç' -> bin = "10001000";
                case 'è' -> bin = "10001001";
                case 'é' -> bin = "10001010";
                case 'ê' -> bin = "10001011";
                case 'ì' -> bin = "10001100";
                case 'í' -> bin = "10001101";
                case 'î' -> bin = "10001110";
                case 'ï' -> bin = "10001111";
                case 'ð' -> bin = "10010000";
                case 'ñ' -> bin = "10010001";
                case 'ò' -> bin = "10010010";
                case 'ó' -> bin = "10010011";
                case 'ô' -> bin = "10010100";
                case 'õ' -> bin = "10010101";
                case 'ö' -> bin = "10010110";
                case '÷' -> bin = "10010111";
                case 'ø' -> bin = "10011000";
                case 'ù' -> bin = "10011001";
                case 'ú' -> bin = "10011010";
                case 'û' -> bin = "10011011";
                case 'ü' -> bin = "10011100";
                case 'ý' -> bin = "10011101";
                case 'þ' -> bin = "10011110";
                case 'ÿ' -> bin = "10011111";
                case '°' -> bin = "10100000";
                case '┘' -> bin = "10100001";
                case '┌' -> bin = "10100010";
                case '╬' -> bin = "10100011";
                case '╧' -> bin = "10100100";
                case '╨' -> bin = "10100101";
                case '╤' -> bin = "10100110";
                case '╥' -> bin = "10100111";
                case '╙' -> bin = "10101000";
                case '╘' -> bin = "10101001";
                case '╒' -> bin = "10101010";
                case '╓' -> bin = "10101011";
                case '╫' -> bin = "10101100";
                case '╪' -> bin = "10101101";
                case '∙' -> bin = "10101110";
                case '║' -> bin = "10101111";
                case '╗' -> bin = "10110000";
                case '╝' -> bin = "10110001";
                case '╜' -> bin = "10110010";
                case '╛' -> bin = "10110011";
                case '┐' -> bin = "10110100";
                case '└' -> bin = "10110101";
                case '┴' -> bin = "10110110";
                case '┬' -> bin = "10110111";
                case '├' -> bin = "10111000";
                case '│' -> bin = "10111001";
                case '┤' -> bin = "10111010";
                case '╡' -> bin = "10111011";
                case '╢' -> bin = "10111100";
                case '╖' -> bin = "10111101";
                case '╕' -> bin = "10111110";
                case '╣' -> bin = "10111111";
                case 'А' -> bin = "11000000";
                case 'Б' -> bin = "11000001";
                case 'В' -> bin = "11000010";
                case 'Г' -> bin = "11000011";
                case 'Д' -> bin = "11000100";
                case 'Е' -> bin = "11000101";
                case 'Ж' -> bin = "11000110";
                case 'З' -> bin = "11000111";
                case 'И' -> bin = "11001000";
                case 'Й' -> bin = "11001001";
                case 'К' -> bin = "11001010";
                case 'Л' -> bin = "11001011";
                case 'М' -> bin = "11001100";
                case 'Н' -> bin = "11001101";
                case 'О' -> bin = "11001110";
                case 'П' -> bin = "11001111";
                case 'Р' -> bin = "11010000";
                case 'С' -> bin = "11010001";
                case 'Т' -> bin = "11010010";
                case 'У' -> bin = "11010011";
                case 'Ф' -> bin = "11010100";
                case 'Х' -> bin = "11010101";
                case 'Ц' -> bin = "11010110";
                case 'Ч' -> bin = "11010111";
                case 'Ш' -> bin = "11011000";
                case 'Щ' -> bin = "11011001";
                case 'Ъ' -> bin = "11011010";
                case 'Ы' -> bin = "11011011";
                case 'Ь' -> bin = "11011100";
                case 'Э' -> bin = "11011101";
                case 'Ю' -> bin = "11011110";
                case 'Я' -> bin = "11011111";
                case 'а' -> bin = "11100000";
                case 'б' -> bin = "11100001";
                case 'в' -> bin = "11100010";
                case 'г' -> bin = "11100011";
                case 'д' -> bin = "11100100";
                case 'е' -> bin = "11100101";
                case 'ж' -> bin = "11100110";
                case 'з' -> bin = "11100111";
                case 'и' -> bin = "11101000";
                case 'й' -> bin = "11101001";
                case 'к' -> bin = "11101010";
                case 'л' -> bin = "11101011";
                case 'м' -> bin = "11101100";
                case 'н' -> bin = "11101101";
                case 'о' -> bin = "11101110";
                case 'п' -> bin = "11101111";
                case 'р' -> bin = "11110000";
                case 'с' -> bin = "11110001";
                case 'т' -> bin = "11110010";
                case 'у' -> bin = "11110011";
                case 'ф' -> bin = "11110100";
                case 'х' -> bin = "11110101";
                case 'ц' -> bin = "11110110";
                case 'ч' -> bin = "11110111";
                case 'ш' -> bin = "11111000";
                case 'щ' -> bin = "11111001";
                case 'ъ' -> bin = "11111010";
                case 'ы' -> bin = "11111011";
                case 'ь' -> bin = "11111100";
                case 'э' -> bin = "11111101";
                case 'ю' -> bin = "11111110";
                case 'я' -> bin = "11111111";
                default -> bin = "ERROR!";
            }
            stringBuilder.append(bin);
        }
        return stringBuilder.toString();
    }

    public static String binaryToString(String bin) {
        String str;
        switch (bin) {
            case "00000000" -> str = "Ё";
            case "00000001" -> str = "ё";
            case "00000010" -> str = "¿";
            case "00000011" -> str = "À";
            case "00000100" -> str = "Á";
            case "00000101" -> str = "Â";
            case "00000110" -> str = "Ã";
            case "00000111" -> str = "Ä";
            case "00001000" -> str = "Å";
            case "00001001" -> str = "Æ";
            case "00001010" -> str = "Ç";
            case "00001011" -> str = "È";
            case "00001100" -> str = "É";
            case "00001101" -> str = "Ê";
            case "00001110" -> str = "Ì";
            case "00001111" -> str = "Í";
            case "00010000" -> str = "Î";
            case "00010001" -> str = "Ï";
            case "00010010" -> str = "Ð";
            case "00010011" -> str = "Ñ";
            case "00010100" -> str = "Ò";
            case "00010101" -> str = "Ó";
            case "00010110" -> str = "Ô";
            case "00010111" -> str = "Õ";
            case "00011000" -> str = "Ö";
            case "00011001" -> str = "×";
            case "00011010" -> str = "Ø";
            case "00011011" -> str = "Ù";
            case "00011100" -> str = "Ú";
            case "00011101" -> str = "Û";
            case "00011110" -> str = "Ü";
            case "00011111" -> str = "Ý";
            case "00100000" -> str = " ";
            case "00100001" -> str = "!";
            case "00100010" -> str = "\"";
            case "00100011" -> str = "#";
            case "00100100" -> str = "$";
            case "00100101" -> str = "%";
            case "00100110" -> str = "&";
            case "00100111" -> str = "'";
            case "00101000" -> str = "(";
            case "00101001" -> str = ")";
            case "00101010" -> str = "*";
            case "00101011" -> str = "+";
            case "00101100" -> str = ",";
            case "00101101" -> str = "-";
            case "00101110" -> str = ".";
            case "00101111" -> str = "/";
            case "00110000" -> str = "0";
            case "00110001" -> str = "1";
            case "00110010" -> str = "2";
            case "00110011" -> str = "3";
            case "00110100" -> str = "4";
            case "00110101" -> str = "5";
            case "00110110" -> str = "6";
            case "00110111" -> str = "7";
            case "00111000" -> str = "8";
            case "00111001" -> str = "9";
            case "00111010" -> str = ":";
            case "00111011" -> str = ";";
            case "00111100" -> str = "<";
            case "00111101" -> str = "=";
            case "00111110" -> str = ">";
            case "00111111" -> str = "?";
            case "01000000" -> str = "@";
            case "01000001" -> str = "A";
            case "01000010" -> str = "B";
            case "01000011" -> str = "C";
            case "01000100" -> str = "D";
            case "01000101" -> str = "E";
            case "01000110" -> str = "F";
            case "01000111" -> str = "G";
            case "01001000" -> str = "H";
            case "01001001" -> str = "I";
            case "01001010" -> str = "J";
            case "01001011" -> str = "K";
            case "01001100" -> str = "L";
            case "01001101" -> str = "M";
            case "01001110" -> str = "N";
            case "01001111" -> str = "O";
            case "01010000" -> str = "P";
            case "01010001" -> str = "Q";
            case "01010010" -> str = "R";
            case "01010011" -> str = "S";
            case "01010100" -> str = "T";
            case "01010101" -> str = "U";
            case "01010110" -> str = "V";
            case "01010111" -> str = "W";
            case "01011000" -> str = "X";
            case "01011001" -> str = "Y";
            case "01011010" -> str = "Z";
            case "01011011" -> str = "[";
            case "01011100" -> str = "\\";
            case "01011101" -> str = "]";
            case "01011110" -> str = "^";
            case "01011111" -> str = "_";
            case "01100000" -> str = "`";
            case "01100001" -> str = "a";
            case "01100010" -> str = "b";
            case "01100011" -> str = "c";
            case "01100100" -> str = "d";
            case "01100101" -> str = "e";
            case "01100110" -> str = "f";
            case "01100111" -> str = "g";
            case "01101000" -> str = "h";
            case "01101001" -> str = "i";
            case "01101010" -> str = "j";
            case "01101011" -> str = "k";
            case "01101100" -> str = "l";
            case "01101101" -> str = "m";
            case "01101110" -> str = "n";
            case "01101111" -> str = "o";
            case "01110000" -> str = "p";
            case "01110001" -> str = "q";
            case "01110010" -> str = "r";
            case "01110011" -> str = "s";
            case "01110100" -> str = "t";
            case "01110101" -> str = "u";
            case "01110110" -> str = "v";
            case "01110111" -> str = "w";
            case "01111000" -> str = "x";
            case "01111001" -> str = "y";
            case "01111010" -> str = "z";
            case "01111011" -> str = "{";
            case "01111100" -> str = "|";
            case "01111101" -> str = "}";
            case "01111110" -> str = "~";
            case "01111111" -> str = "Þ";
            case "10000000" -> str = "ß";
            case "10000001" -> str = "à";
            case "10000010" -> str = "á";
            case "10000011" -> str = "â";
            case "10000100" -> str = "ã";
            case "10000101" -> str = "ä";
            case "10000110" -> str = "å";
            case "10000111" -> str = "æ";
            case "10001000" -> str = "ç";
            case "10001001" -> str = "è";
            case "10001010" -> str = "é";
            case "10001011" -> str = "ê";
            case "10001100" -> str = "ì";
            case "10001101" -> str = "í";
            case "10001110" -> str = "î";
            case "10001111" -> str = "ï";
            case "10010000" -> str = "ð";
            case "10010001" -> str = "ñ";
            case "10010010" -> str = "ò";
            case "10010011" -> str = "ó";
            case "10010100" -> str = "ô";
            case "10010101" -> str = "õ";
            case "10010110" -> str = "ö";
            case "10010111" -> str = "÷";
            case "10011000" -> str = "ø";
            case "10011001" -> str = "ù";
            case "10011010" -> str = "ú";
            case "10011011" -> str = "û";
            case "10011100" -> str = "ü";
            case "10011101" -> str = "ý";
            case "10011110" -> str = "þ";
            case "10011111" -> str = "ÿ";
            case "10100000" -> str = "°";
            case "10100001" -> str = "┘";
            case "10100010" -> str = "┌";
            case "10100011" -> str = "╬";
            case "10100100" -> str = "╧";
            case "10100101" -> str = "╨";
            case "10100110" -> str = "╤";
            case "10100111" -> str = "╥";
            case "10101000" -> str = "╙";
            case "10101001" -> str = "╘";
            case "10101010" -> str = "╒";
            case "10101011" -> str = "╓";
            case "10101100" -> str = "╫";
            case "10101101" -> str = "╪";
            case "10101110" -> str = "∙";
            case "10101111" -> str = "║";
            case "10110000" -> str = "╗";
            case "10110001" -> str = "╝";
            case "10110010" -> str = "╜";
            case "10110011" -> str = "╛";
            case "10110100" -> str = "┐";
            case "10110101" -> str = "└";
            case "10110110" -> str = "┴";
            case "10110111" -> str = "┬";
            case "10111000" -> str = "├";
            case "10111001" -> str = "│";
            case "10111010" -> str = "┤";
            case "10111011" -> str = "╡";
            case "10111100" -> str = "╢";
            case "10111101" -> str = "╖";
            case "10111110" -> str = "╕";
            case "10111111" -> str = "╣";
            case "11000000" -> str = "А";
            case "11000001" -> str = "Б";
            case "11000010" -> str = "В";
            case "11000011" -> str = "Г";
            case "11000100" -> str = "Д";
            case "11000101" -> str = "Е";
            case "11000110" -> str = "Ж";
            case "11000111" -> str = "З";
            case "11001000" -> str = "И";
            case "11001001" -> str = "Й";
            case "11001010" -> str = "К";
            case "11001011" -> str = "Л";
            case "11001100" -> str = "М";
            case "11001101" -> str = "Н";
            case "11001110" -> str = "О";
            case "11001111" -> str = "П";
            case "11010000" -> str = "Р";
            case "11010001" -> str = "С";
            case "11010010" -> str = "Т";
            case "11010011" -> str = "У";
            case "11010100" -> str = "Ф";
            case "11010101" -> str = "Х";
            case "11010110" -> str = "Ц";
            case "11010111" -> str = "Ч";
            case "11011000" -> str = "Ш";
            case "11011001" -> str = "Щ";
            case "11011010" -> str = "Ъ";
            case "11011011" -> str = "Ы";
            case "11011100" -> str = "Ь";
            case "11011101" -> str = "Э";
            case "11011110" -> str = "Ю";
            case "11011111" -> str = "Я";
            case "11100000" -> str = "а";
            case "11100001" -> str = "б";
            case "11100010" -> str = "в";
            case "11100011" -> str = "г";
            case "11100100" -> str = "д";
            case "11100101" -> str = "е";
            case "11100110" -> str = "ж";
            case "11100111" -> str = "з";
            case "11101000" -> str = "и";
            case "11101001" -> str = "й";
            case "11101010" -> str = "к";
            case "11101011" -> str = "л";
            case "11101100" -> str = "м";
            case "11101101" -> str = "н";
            case "11101110" -> str = "о";
            case "11101111" -> str = "п";
            case "11110000" -> str = "р";
            case "11110001" -> str = "с";
            case "11110010" -> str = "т";
            case "11110011" -> str = "у";
            case "11110100" -> str = "ф";
            case "11110101" -> str = "х";
            case "11110110" -> str = "ц";
            case "11110111" -> str = "ч";
            case "11111000" -> str = "ш";
            case "11111001" -> str = "щ";
            case "11111010" -> str = "ъ";
            case "11111011" -> str = "ы";
            case "11111100" -> str = "ь";
            case "11111101" -> str = "э";
            case "11111110" -> str = "ю";
            case "11111111" -> str = "я";
            default -> str = "ERROR!";
        }
        return str;
    }

    public static String caesar(String str, int g, boolean ed) {
        ArrayList<String> alphabet = new ArrayList<>(Arrays.asList("00000000", "00000001", "00000010", "00000011", "00000100", "00000101", "00000110",
                "00000111", "00001000", "00001001", "00001010", "00001011", "00001100", "00001101", "00001110", "00001111", "00010000", "00010001", "00010010",
                "00010011", "00010100", "00010101", "00010110", "00010111", "00011000", "00011001", "00011010", "00011011", "00011100", "00011101", "00011110",
                "00011111", "00100000", "00100001", "00100010", "00100011", "00100100", "00100101", "00100110", "00100111", "00101000", "00101001", "00101010",
                "00101011", "00101100", "00101101", "00101110", "00101111", "00110000", "00110001", "00110010", "00110011", "00110100", "00110101", "00110110",
                "00110111", "00111000", "00111001", "00111010", "00111011", "00111100", "00111101", "00111110", "00111111", "01000000", "01000001", "01000010",
                "01000011", "01000100", "01000101", "01000110", "01000111", "01001000", "01001001", "01001010", "01001011", "01001100", "01001101", "01001110",
                "01001111", "01010000", "01010001", "01010010", "01010011", "01010100", "01010101", "01010110", "01010111", "01011000", "01011001", "01011010",
                "01011011", "01011100", "01011101", "01011110", "01011111", "01100000", "01100001", "01100010", "01100011", "01100100", "01100101", "01100110",
                "01100111", "01101000", "01101001", "01101010", "01101011", "01101100", "01101101", "01101110", "01101111", "01110000", "01110001", "01110010",
                "01110011", "01110100", "01110101", "01110110", "01110111", "01111000", "01111001", "01111010", "01111011", "01111100", "01111101", "01111110",
                "01111111", "10000000", "10000001", "10000010", "10000011", "10000100", "10000101", "10000110", "10000111", "10001000", "10001001", "10001010",
                "10001011", "10001100", "10001101", "10001110", "10001111", "10010000", "10010001", "10010010", "10010011", "10010100", "10010101", "10010110",
                "10010111", "10011000", "10011001", "10011010", "10011011", "10011100", "10011101", "10011110", "10011111", "10100000", "10100001", "10100010",
                "10100011", "10100100", "10100101", "10100110", "10100111", "10101000", "10101001", "10101010", "10101011", "10101100", "10101101", "10101110",
                "10101111", "10110000", "10110001", "10110010", "10110011", "10110100", "10110101", "10110110", "10110111", "10111000", "10111001", "10111010",
                "10111011", "10111100", "10111101", "10111110", "10111111", "11000000", "11000001", "11000010", "11000011", "11000100", "11000101", "11000110",
                "11000111", "11001000", "11001001", "11001010", "11001011", "11001100", "11001101", "11001110", "11001111", "11010000", "11010001", "11010010",
                "11010011", "11010100", "11010101", "11010110", "11010111", "11011000", "11011001", "11011010", "11011011", "11011100", "11011101", "11011110",
                "11011111", "11100000", "11100001", "11100010", "11100011", "11100100", "11100101", "11100110", "11100111", "11101000", "11101001", "11101010",
                "11101011", "11101100", "11101101", "11101110", "11101111", "11110000", "11110001", "11110010", "11110011", "11110100", "11110101", "11110110",
                "11110111", "11111000", "11111001", "11111010", "11111011", "11111100", "11111101", "11111110", "11111111"));
        StringBuilder buildLetter = new StringBuilder();
        StringBuilder buildText = new StringBuilder();
        String letter;
        int letterIndex;
        if (ed) {
            for (int i = 0; i < str.length(); i = i + 8) {
                for (int j = 0; j < 8; j++) {
                    buildLetter.append(str.charAt(i + j));
                }
                letter = buildLetter.toString();
                letterIndex = alphabet.indexOf(letter);
                letterIndex = letterIndex - g;
                letter = switch (letterIndex) {
                    case -1 -> alphabet.get(255);
                    case -2 -> alphabet.get(254);
                    case -3 -> alphabet.get(253);
                    default -> alphabet.get(letterIndex);
                };
                buildText.append(letter);
                buildLetter = new StringBuilder();
            }
        } else {
            for (int i = 0; i < str.length(); i = i + 8) {
                for (int j = 0; j < 8; j++) {
                    buildLetter.append(str.charAt(i + j));
                }
                letter = buildLetter.toString();
                letterIndex = alphabet.indexOf(letter);
                letterIndex = letterIndex + g;
                letter = switch (letterIndex) {
                    case 256 -> alphabet.get(0);
                    case 257 -> alphabet.get(1);
                    case 258 -> alphabet.get(2);
                    default -> alphabet.get(letterIndex);
                };
                buildText.append(letter);
                buildLetter = new StringBuilder();
            }
        }
        return buildText.toString();
    }
}
