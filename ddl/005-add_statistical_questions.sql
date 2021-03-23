create definer = admin@`%` trigger add_statistical_questions
	after insert
	on questionnaire
	for each row
	begin
    INSERT INTO questionnaire_to_question
    SELECT new.id, question.id FROM question
    WHERE optional = true;
end;

