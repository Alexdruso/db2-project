create definer = admin@`%` trigger check_offensive_words_update
	after update
	on answer
	for each row
	begin

        if exists(
            select *
            from db2.offensive_word ow
            where upper(new.text) like upper(ow.word)
            )
            then

            signal sqlstate '40666' set message_text = 'Bad word detected';

            update db2.user
                set ban = true
            where id = any (
                select user_id
                from questionnaire_submission
                where questionnaire_submission.id = new.questionnaire_submission_id
                );

            delete from questionnaire_submission
                where questionnaire_submission.id = new.questionnaire_submission_id;


        end if;

    end;

