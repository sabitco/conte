package co.org.conte.sgm.util;

import edu.vt.middleware.crypt.digest.MD5;
import edu.vt.middleware.crypt.util.Convert;
import edu.vt.middleware.crypt.util.HexConverter;
import edu.vt.middleware.dictionary.ArrayWordList;
import edu.vt.middleware.dictionary.WordListDictionary;
import edu.vt.middleware.dictionary.sort.ArraysSort;
import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DictionarySubstringRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.HistoryRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.MessageResolver;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Roger Padilla C.
 */
public class MyPasswordValidator {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private MessageResolver msgResolver;
  private List<Rule> ruleList;

  private MyPasswordValidator() {
    initMessageResolver();
    initRuleList();
  }

  private void initMessageResolver() {
    Properties props = new Properties();
    InputStream is = null;
    try {
      is = MyPasswordValidator.class.getResourceAsStream("/password_messages.properties");
      props.load(is);
    } catch (IOException exc) {
      logger.error(exc.getMessage(), exc);
//      throw new InfovalmerSistemaException(exc.getMessage(), exc);
    } finally {
      try {
        is.close();
      } catch (IOException exc) {
        logger.error(exc.getMessage(), exc);
      }
    }
    msgResolver = new MessageResolver(props);
  }

  private void initRuleList() {

    // don't allow whitespace
    WhitespaceRule whitespaceRule = new WhitespaceRule();

    // don't allow numerical sequences of length 3
    NumericalSequenceRule numSeqRule = new NumericalSequenceRule(4, false);

    // don't allow alphabetical sequences
    AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();

    // don't allow 4 repeat characters
    RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);

    // password must be between 8 and 16 chars long
    LengthRule lengthRule = new LengthRule(8, 16);

    // create a case sensitive word list and sort it
    ArrayWordList awl = new ArrayWordList(
            new String[]{"dKywjl-S"},
            false,
            new ArraysSort());

    // create a dictionary for searching
    WordListDictionary dict = new WordListDictionary(awl);

    DictionarySubstringRule dictRule = new DictionarySubstringRule(dict);
    dictRule.setWordLength(4); // size of words to check in the password
    dictRule.setMatchBackwards(true); // match dictionary words backwards

    HistoryRule historyRule = new HistoryRule();
    historyRule.setDigest("MD5", new HexConverter());

    // control allowed characters
    CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
    // require at least 1 digit in passwords
    charRule.getRules().add(new DigitCharacterRule(1));
    // require at least 1 non-alphanumeric char
    charRule.getRules().add(new NonAlphanumericCharacterRule(1));
    // require at least 1 upper case char
    charRule.getRules().add(new UppercaseCharacterRule(1));
    // require at least 1 lower case char
    charRule.getRules().add(new LowercaseCharacterRule(1));
    // require at least 3 of the previous rules be met
    charRule.setNumberOfCharacteristics(3);

    // group all rules together in a List
    ruleList = new ArrayList<Rule>();
    ruleList.add(whitespaceRule);
    ruleList.add(numSeqRule);
    ruleList.add(alphaSeqRule);
    ruleList.add(repeatRule);
    ruleList.add(lengthRule);
    ruleList.add(dictRule);
    ruleList.add(historyRule);
    ruleList.add(charRule);
  }

  public ValidationResult validate(String password, List<String> history) {

    logger.debug(password);
    logger.debug(history == null ? "history NULL" : history.toString());

    PasswordValidator validator = new PasswordValidator(msgResolver, ruleList);
    PasswordData passwordData = new PasswordData(new Password(password));
    passwordData.setPasswordHistory(history);

    RuleResult result = validator.validate(passwordData);

    boolean valid = result.isValid();
    List<String> messages = validator.getMessages(result);

    ValidationResult validationResult = new ValidationResult(valid, messages);

    return validationResult;
  }

  public boolean validar(String rawPassword, String encryptedPassword) {
    String hash = new MD5().digest(Convert.toBytes(rawPassword), new HexConverter());
    logger.debug(rawPassword);
    logger.debug(encryptedPassword);
    return hash.equals(encryptedPassword);
  }

  public static MyPasswordValidator getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private static class SingletonHolder {
    public static final MyPasswordValidator INSTANCE = new MyPasswordValidator();
  }

  public class ValidationResult {

    private boolean valid;
    private List<String> messages;

    public ValidationResult(boolean valid, List<String> messages) {
      this.valid = valid;
      this.messages = messages;
    }

    public boolean isValid() {
      return valid;
    }

    public List<String> getMessages() {
      return messages;
    }

  }
}
