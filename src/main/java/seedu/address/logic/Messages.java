package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command. \n"
            + "Possible commands include: add, addnote, edit, delete, find, show, list, help, exit.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT_DELETE = "Invalid command format! "
            + "An IC number should follow behind delete. \n%1$s";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT_FIND = "Invalid command format! "
            + "An IC number should follow behind find. \n%1$s";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT_ADD = "Invalid command format! "
            + "All fields are required but may not necessary be in order. \n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NO_MATCHING_IC = "There is nobody with the IC you specified. \n"
            + "You may use the add command to add the specific person to the address book.";
    public static final String MESSAGE_NO_MATCHING_IC_DELETE = "There is nobody with the IC you specified. \n";


    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Identity Card Number: ")
                .append(person.getIdentityCardNumber())
                .append("; Age: ")
                .append(person.getAge())
                .append("; Sex: ")
                .append(person.getSex())
                .append("; Note: ")
                .append(person.getNote())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
