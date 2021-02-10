create definer = root@localhost trigger add_point
	after insert
	on answer
	for each row
	begin
        
        set @point = 1;

        if (select optional from question where question.id = new.question_id) = true
        then set @point = 2;
        end if;

        update questionnaire_submission
            set points = points + @point
            where id = NEW.questionnaire_submission_id;

    end;

