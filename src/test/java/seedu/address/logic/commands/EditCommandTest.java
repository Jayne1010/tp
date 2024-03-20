package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.IdentityCardNumber;
import seedu.address.model.person.IdentityCardNumberMatchesPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
@Disabled("Requires edit Command to be implemented")
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // Assuming there's at least one person in the model's person list.
        Person personToEdit = model.getFilteredPersonList().get(0);
        IdentityCardNumber icNumberToEdit = personToEdit.getIdentityCardNumber();

        // Assuming editedPerson is a different person object with updated fields.
        Person editedPerson = new PersonBuilder().withIdentityCardNumber(icNumberToEdit.toString()).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();

        // Use IdentityCardNumberMatchesPredicate to find the person to edit.
        EditCommand editCommand = new EditCommand(new IdentityCardNumberMatchesPredicate(icNumberToEdit), descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson); // This method might need to be adjusted to match your implementation.

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        // Assuming there's at least one person in the address book
        Person lastPerson = model.getAddressBook().getPersonList().get(model.getAddressBook().getPersonList().size() - 1);

        // Pick an IC number that matches the last person
        IdentityCardNumber icNumber = lastPerson.getIdentityCardNumber();
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Build the edited person with the specified fields
        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();

        // Create an EditCommand with the edited person and the IC number predicate
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB)
                        .withPhone(VALID_PHONE_BOB)
                        .withTags(VALID_TAG_HUSBAND)
                        .build());

        // Expected success message
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        // Create an expected model with the edited person
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson); // Ensure the person is updated in the expected model

        // Assert that the command is successful and the model is updated as expected
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        // Assuming there's at least one person in the address book
        Person editedPerson = model.getAddressBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Pick an IC number that matches the edited person
        IdentityCardNumber icNumber = editedPerson.getIdentityCardNumber();
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Create an EditCommand with no fields specified and the IC number predicate
        EditCommand editCommand = new EditCommand(predicate, new EditPersonDescriptor());

        // Expected success message
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        // Create an expected model with the edited person
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(editedPerson, editedPerson); // Ensure the person is updated in the expected model

        // Assert that the command is successful and the model is updated as expected
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        // Show a single person in the filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Get the person from the filtered list
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Assuming there's at least one person in the address book
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();

        // Pick an IC number that matches the edited person
        IdentityCardNumber icNumber = editedPerson.getIdentityCardNumber();
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Create an EditCommand with the edited person and the IC number predicate
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        // Expected success message
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        // Create an expected model with the edited person
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInFilteredList, editedPerson); // Update the person in the expected model

        // Assert that the command is successful and the model is updated as expected
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        // Assuming there's at least one person in the address book
        Person firstPerson = model.getAddressBook().getPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Create a duplicate of the first person
        Person duplicatePerson = new PersonBuilder(firstPerson).build();

        // Pick an IC number that matches the edited person
        IdentityCardNumber icNumber = duplicatePerson.getIdentityCardNumber();
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Create an EditCommand with the duplicate person
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder(duplicatePerson).build());

        // Execute the command and ensure it fails with the appropriate message
        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        // Show a single person in the filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Assuming there's at least one person in the address book
        Person personToEdit = model.getFilteredPersonList().get(0);
        Person duplicatePerson = new PersonBuilder(personToEdit).build(); // Create a duplicate of the person to edit

        // Pick an IC number that matches the edited person
        IdentityCardNumber icNumber = duplicatePerson.getIdentityCardNumber();
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Create an EditCommand with the duplicate person
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder(duplicatePerson).build());

        // Execute the command and ensure it fails with the appropriate message
        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        // Create an IC number that does not match any person in the unfiltered list
        String icNumberStr = "S9876543B"; // Example IC number
        IdentityCardNumber icNumber = new IdentityCardNumber(icNumberStr);
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Ensure the chosen IC number does not match any person in the unfiltered list
        assertFalse(predicate.test(model.getAddressBook().getPersonList().get(0))); // Assuming there's at least one person in the list

        // Create an EditCommand with the out of bound IC number
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        // Execute the command and ensure it fails with the appropriate message
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where IC number does not match any person in the filtered list,
     * but matches a person in the address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        // Show a single person in the filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // Pick an IC number not present in the filtered list
        String icNumberStr = "S9876543B"; // Example IC number
        IdentityCardNumber icNumber = new IdentityCardNumber(icNumberStr);
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);

        // Ensure the chosen IC number is within the bounds of the address book list
        assertTrue(predicate.test(model.getAddressBook().getPersonList().get(0))); // Assuming there's at least one person in the list

        // Create an EditCommand with the out of bound IC number
        EditCommand editCommand = new EditCommand(predicate,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        // Execute the command and ensure it fails with the appropriate message
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        IdentityCardNumber icNumberAmy = new IdentityCardNumber("S1234567A"); // Example IC number
        IdentityCardNumber icNumberBob = new IdentityCardNumber("S9876543B"); // Another example IC number
        IdentityCardNumberMatchesPredicate predicateAmy = new IdentityCardNumberMatchesPredicate(icNumberAmy);
        IdentityCardNumberMatchesPredicate predicateBob = new IdentityCardNumberMatchesPredicate(icNumberBob);

        final EditCommand standardCommand = new EditCommand(predicateAmy, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(predicateAmy, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different predicate -> returns false
        assertFalse(standardCommand.equals(new EditCommand(predicateBob, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(predicateAmy, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        // Assume a valid IC number string for the test
        String icNumberStr = "S1234567A";
        IdentityCardNumber icNumber = new IdentityCardNumber(icNumberStr);
        IdentityCardNumberMatchesPredicate predicate = new IdentityCardNumberMatchesPredicate(icNumber);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(predicate, editPersonDescriptor);

        // Adjust the expected string to match the new constructor parameters
        String expected = EditCommand.class.getCanonicalName() + "{predicate=" + predicate + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
