drop table if exists posts;
drop table if exists business_status;
drop table if exists business_hours;
drop table if exists user_favorites;
drop table if exists business_reviews;
drop table if exists user_status;
drop table if exists business;
drop table if exists app_users;
drop table if exists user_roles;



create table user_roles (
	role_id serial,
	name varchar(35) not null,
	
	constraint user_roles_app_users_pk
	primary key (role_id)
);

create table app_users (
	user_id serial,
	username varchar(25) not null unique,
	password varchar(25) not null,
	email varchar(25) not null unique,
	phone_number varchar(25) not null,
	firstname varchar(25) not null, 
	lastname varchar(25) not null,
	register_datetime timestamp default localtimestamp,
	user_role_id int,
	is_active boolean,
	
	constraint app_users_pk
	primary key (user_id),
	
	constraint app_user_role_fk
	foreign key (user_role_id) references user_roles
);

insert into user_roles (name) values
('Admin'),
('Owner'),
('User');

create table business (
	business_id serial,
	owner_id int,
	email varchar(320) not null unique,
	business_name varchar(25) not null,
	location varchar(100) not null,
	business_type varchar,
	register_datetime timestamp default localtimestamp,
	is_active boolean,
	
	constraint business_pk
	primary key (business_id),
	
	constraint business_owners_fk
	foreign key (owner_id)
	references app_users
);

create table posts (
  post_id serial,
  business_id int,
  post_type int,
  body varchar(2056) not null,
  created_time timestamp default localtimestamp,
  
  constraint posts_pk
  primary key (post_id),
  
  constraint posts_business_fk
  foreign key (business_id)
  references business
);

create table business_hours (
	hours_id serial,
	business_id int not null,
	day int not null,
	open timestamp not null,
	closed timestamp not null,
	
	constraint business_hours_pk
	primary key (hours_id),
	
	constraint business_hours_business_fk
	foreign key (business_id)
	references business
);

create table user_favorites (
	user_id int,
	business_id int,
	
	constraint user_favorites_pk
	primary key (user_id, business_id),
	
	constraint user_favorites_app_users_fk
	foreign key (user_id)
	references app_users,
	
	constraint user_favorites_business_fk
	foreign key (business_id)
	references business
);

create table business_reviews (
	review_id serial,
	business_id int not null,
	user_id int not null,
	rating double precision not null,
	review varchar default '',
	
	constraint business_reviews_pk
	primary key (review_id),
	
	constraint business_reviews_business_fk
	foreign key (business_id)
	references business,
	
	constraint business_reviews_app_users_fk
	foreign key (user_id)
	references app_users
);

