-- Cyclic dependencies found

create definer = admin@`%` trigger remove_point
	after delete
	on answer
	for each row
	begin

        set @point = 1;

        if (select optional from db2.question where db2.question.id = old.question_id) = true
        then set @point = 2;
        end if;

        update db2.questionnaire_submission
            set points = points - @point
            where id = old.questionnaire_submission_id;

    end;

