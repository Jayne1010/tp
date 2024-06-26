package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.IdentityCardNumberMatchesPredicate;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Changes the note of an existing person in the address book.
 */
public class AddNoteCommand extends Command {

    public static final String COMMAND_WORD = "addnote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the note of the person whose profile matches "
            + "the specified IC (must be a valid identity card number). \n"
            + "Existing remark will be appended by default. \n"
            + "Parameters: "
            + "IC "
            + PREFIX_NOTE + "NOTE \n"
            + "Example: " + COMMAND_WORD
            + " S0123456Q "
            + PREFIX_NOTE + "Diabetes \n"
            + "To replace the original note, add -replace at the end of your command. \n"
            + "Example: " + COMMAND_WORD
            + " S0123456Q "
            + PREFIX_NOTE + "Diabetes -replace";

    public static final String MESSAGE_MODIFY_NOTE_SUCCESS = "Note for %1$s (IC: %2$s) modified successfully!";
    private static final Logger logger = LogsCenter.getLogger(AddNoteCommand.class);

    private final IdentityCardNumberMatchesPredicate icPredicate;
    private final Note note;
    private final boolean isReplace;

    /**
     * @param icPredicate of the person in the filtered person list to edit the note
     * @param note of the person to be updated to
     */
    public AddNoteCommand(IdentityCardNumberMatchesPredicate icPredicate, Note note, boolean isReplace) {
        requireAllNonNull(icPredicate, note);

        this.icPredicate = icPredicate;
        this.note = note;
        this.isReplace = isReplace;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.getPersonIfExists(icPredicate)
                .orElseThrow(() -> new CommandException(Messages.MESSAGE_NO_MATCHING_IC));

        Person editedPerson;

        if (isReplace || personToEdit.getNote().equals(Note.DEFAULT)) {
            editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getIdentityCardNumber(), personToEdit.getAge(), personToEdit.getSex(),
                    personToEdit.getAddress(), note);
        } else {
            Note updatedNote = personToEdit.getNote().append("\n" + note.toString());
            editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getIdentityCardNumber(), personToEdit.getAge(), personToEdit.getSex(),
                    personToEdit.getAddress(), updatedNote);
        }

        model.setPerson(personToEdit, editedPerson);

        if (model.isPersonDisplayed(personToEdit)) {
            model.setDisplayNote(editedPerson);
        }

        logger.info("Addnote command has been successfully executed on Person with IC: "
                + personToEdit.getIdentityCardNumber());
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Checks if the note of the person is changed.
     * @param person the person to be compared with
     * @param newNote the new note to be compared with
     * @return true if the note is changed, false otherwise
     */
    public boolean isNoteChanged(Person person, Note newNote) {
        if (person != null) {
            return !person.getNote().equals(newNote);
        }
        return false;
    }

    /**
     * Generates a command execution success message based on whether the remark is added to or removed from
     * {@code personToEdit}.
     */
    public String generateSuccessMessage(Person personToEdit) {
        return String.format(MESSAGE_MODIFY_NOTE_SUCCESS, personToEdit.getName(),
                personToEdit.getIdentityCardNumber());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddNoteCommand)) {
            return false;
        }

        AddNoteCommand e = (AddNoteCommand) other;
        return icPredicate.equals(e.icPredicate)
                && note.equals(e.note)
                && isReplace == e.isReplace;
    }
}
