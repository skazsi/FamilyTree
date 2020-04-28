package hu.skzs.familytree.renderer;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GridLayouttTest {

	@Test
	void constructor_NullImageProvider() {
		// When
		Throwable throwable = catchThrowable(() -> new Renderer(null));

		// Then
		then(throwable).isInstanceOf(NullPointerException.class);
	}
}
