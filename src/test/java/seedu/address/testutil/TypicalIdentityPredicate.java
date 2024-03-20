package seedu.address.testutil;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.IdentityCardNumber;
import seedu.address.model.person.IdentityCardNumberMatchesPredicate;

public class TypicalIdentityPredicate {
    public static final IdentityCardNumberMatchesPredicate IC_AMY =
            new IdentityCardNumberMatchesPredicate(new IdentityCardNumber("S1234567A"));
    public static final IdentityCardNumberMatchesPredicate IC_BOB =
            new IdentityCardNumberMatchesPredicate(new IdentityCardNumber("S1234567B"));
}

