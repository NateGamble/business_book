insert into app_users (username, password, email, phone_number, firstname, lastname, user_role_id, is_active) values
('ngamble', 'pass', 'nathan.gamble@revature.net', '9168500634', 'Nate', 'Gamble', 1, true),
('testUser2', 'pass', 'test.user@revature.net', '8199647755', 'test', 'user', 3, true);

insert into business (owner_id, email, business_name, location, business_type, is_active) values
(1, 'nathan.gamble@revature.net', 'testBusiness', 'here', 'fake', true);

insert into posts (business_id, post_type, body) values
(1, 1, 'Body of a definitely real post');

insert into business_hours (business_id, day, open, closed) values
(1, 1, '2021-03-01 09:00:00-00', '2021-03-01 21:00:00-00');


insert into user_favorites (user_id, business_id) values
(2, 1);

insert into business_reviews (business_id, user_id, rating, review) values
(1, 2, 5.0, 'Best fake business I have been to!');

commit;