package hu.skzs.familytree;

import static org.mockito.BDDMockito.given;

import java.util.Date;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberComparatorTest {

	private MemberComparator underTest = new MemberComparator();

	@Mock
	private Person person1, person2;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Couple couple1, couple2;

	@Test
	void compare_TwoPersons() {
		// Given
		given(person1.getBirthDate()).willReturn(new Date(30));
		given(person2.getBirthDate()).willReturn(new Date(40));

		// When
		int compare = underTest.compare(person1, person2);

		// Then
		BDDAssertions.then(compare).isNegative();
	}

	@Test
	void compare_TwoPersons_OtherOrder() {
		// Given
		given(person1.getBirthDate()).willReturn(new Date(30));
		given(person2.getBirthDate()).willReturn(new Date(40));

		// When
		int compare = underTest.compare(person2, person1);

		// Then
		BDDAssertions.then(compare).isPositive();
	}

	@Test
	void compare_TwoCouple() {
		// Given
		given(couple1.getPerson().getBirthDate()).willReturn(new Date(30));
		given(couple2.getPerson().getBirthDate()).willReturn(new Date(40));

		// When
		int compare = underTest.compare(couple1, couple2);

		// Then
		BDDAssertions.then(compare).isNegative();
	}

	@Test
	void compare_TwoCouple_OtherOrder() {
		// Given
		given(couple1.getPerson().getBirthDate()).willReturn(new Date(30));
		given(couple2.getPerson().getBirthDate()).willReturn(new Date(40));

		// When
		int compare = underTest.compare(couple2, couple1);

		// Then
		BDDAssertions.then(compare).isPositive();
	}

	@Test
	void compare_OnePersonOneCouple_Equals() {
		// Given
		given(person1.getBirthDate()).willReturn(new Date(30));
		given(couple1.getPerson().getBirthDate()).willReturn(new Date(30));
		given(person1.getName()).willReturn("zzz");
		given(couple1.getPerson().getName()).willReturn("aaa");

		// When
		int compare = underTest.compare(person1, couple1);

		// Then
		BDDAssertions.then(compare).isPositive();
	}
}
