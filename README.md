# booking


Java version : 1.8
Spring Boot Application
Mysql version : 8.0.12
Port : 8081

main method : com.hotels.booking.BookingApplication


Sample Request :

curl -X POST \
  http://localhost:8081/v1/availability \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: b81fc0c2-b5f0-4a71-89c2-7acd2269400f' \
  -H 'cache-control: no-cache' \
  -d '{
	"city":"dubai",
	"date":{
		"start":"2021-07-21",
		"end":"2021-07-28"
	},
	"flexible":{
		"type":"month",
		"month":["jul"]
	},
	"apartmentType":"1bhr",
	"amenities":["WiFi"]
}'



Mysql schema :

use hotels;
-- each property is attached to a building (1 to 1)

create table hotels.building (
    id int(11) not null AUTO_INCREMENT,
    city ENUM('Dubai', 'Montreal'),
    Primary key (`id`)
);

insert into hotels.building (city) values ('Dubai');
insert into hotels.building (city) values ('Montreal');

-- properties/units
create type hotels.amenities_enum as ENUM ('WiFi', 'Pool', 'Garden', 'Tennis table', 'Parking');
create type hotels.property_type_enum as ENUM ('1bdr', '2bdr', '3bdr');

create table hotels.property (
    id int(11) not null AUTO_INCREMENT,
    building_id int(11) not null,
    title varchar(100) not null,
    property_type ENUM ('1bdr', '2bdr', '3bdr'),
    amenities set ('WiFi', 'Pool', 'Garden', 'Tennis table', 'Parking'),
Primary key (`id`),
Unique key (`title`),
    FOREIGN KEY (building_id) REFERENCES hotels.building(id)
);

insert into hotels.property (building_id, title, property_type, amenities) VALUES (1, 'Unit 1', '1bdr', 'WiFi,Parking');
insert into hotels.property (building_id, title, property_type, amenities) VALUES (1, 'Unit 2', '2bdr', 'WiFi,Tennis table');
insert into hotels.property (building_id, title, property_type, amenities) VALUES (1, 'Unit 3', '3bdr', 'Garden');
insert into hotels.property (building_id, title, property_type, amenities) VALUES (2, 'Unit 4', '1bdr', 'Garden,Pool');


-- reservations per unit
create table hotels.reservation (
    id int(11) not null AUTO_INCREMENT,
    check_in date not null,
    check_out date not null,
    property_id int(11) not null,
    Primary key (`id`),
    FOREIGN KEY (property_id) REFERENCES hotels.property(id)
);

insert into hotels.reservation (check_in, check_out, property_id) VALUES ('2021-05-01', '2021-05-10', 1);
insert into hotels.reservation (check_in, check_out, property_id) VALUES ('2021-06-01', '2021-06-03', 1);
insert into hotels.reservation (check_in, check_out, property_id) VALUES ('2021-06-02', '2021-06-07', 2);


-- manual availability settings
create table hotels.availability (
    id int(11) not null AUTO_INCREMENT,
    property_id  int(11) not null,
    start_date date not null,
    end_date date not null,
    is_blocked boolean default false,
    Primary key (`id`),
    FOREIGN KEY (property_id) REFERENCES hotels.property(id)
);

insert into hotels.availability (property_id, start_date, end_date, is_blocked) values (1, '2021-07-01', '2021-07-20', true);
