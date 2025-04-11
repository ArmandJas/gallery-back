package ins.app.utilities.markers;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, CreateRequest.class})
public interface CreateRequestSequence {
}
