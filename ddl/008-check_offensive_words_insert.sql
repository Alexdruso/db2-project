create definer = admin@`%` trigger check_offensive_words_insert
	before insert
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
        end if;

    end;

