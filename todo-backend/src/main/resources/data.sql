insert into todo (title, status, description)
select 'ログイン機能を実装する', 'open', '認証されたユーザのみアプリケーションにアクセス可能とするため。' from dual
where not exists (
    select * from todo where todo_id = 1
);
insert into todo (title, status, description)
select 'TODO表示機能を実装する', 'doing', '何がTODOとして存在し、どのようなステータスなのかを確認可能とするため' from dual
where not exists (
    select * from todo where todo_id = 2
);