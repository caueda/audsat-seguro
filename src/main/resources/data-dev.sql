-- INSURANCE PARAMS
INSERT INTO insurance_params(ID, INITIAL_AGE, END_AGE, QUOTE, AGGRAVATING_QUOTE, STATUS) VALUES (1, 18, 25, 0.06, 0.02, 'ACTIVE');

-- DRIVERS
INSERT INTO DRIVER (ID, DOCUMENT, BIRTHDATE) VALUES (1, '123.456.789-10', '1990-10-05');
INSERT INTO DRIVER (ID, DOCUMENT, BIRTHDATE) VALUES (2, '999.999.999-99', '2005-12-10');
INSERT INTO DRIVER (ID, DOCUMENT, BIRTHDATE) VALUES (3, '888.888.888-88', '1980-05-21');


-- CUSTOMERS
INSERT INTO CUSTOMER(ID, NAME, DRIVER_ID) VALUES(10, 'Charles Xavier' ,1);
INSERT INTO CUSTOMER(ID, NAME, DRIVER_ID) VALUES(20, 'Jean Grey' ,2);
INSERT INTO CUSTOMER(ID, NAME, DRIVER_ID) VALUES(30, 'Scott Summers' ,3);

-- CARS
INSERT INTO CAR (ID, MODEL, MANUFACTURER, CAR_YEAR, FIPE_VALUE) VALUES (100, 'COROLLA', 'TOYOTA', 2017, 80000);
INSERT INTO CAR (ID, MODEL, MANUFACTURER, CAR_YEAR, FIPE_VALUE) VALUES (110, 'HB20', 'HYUNDAI', 2020, 50000);

-- CAR DRIVERS
INSERT INTO CAR_DRIVER (ID, DRIVER_ID, CAR_ID, IS_MAIN_DRIVER) VALUES (1000, 1, 100, TRUE);
INSERT INTO CAR_DRIVER (ID, DRIVER_ID, CAR_ID, IS_MAIN_DRIVER) VALUES (1002, 2, 100, FALSE);

-- CLAIMS
INSERT INTO CLAIM (ID, CAR_ID, DRIVER_ID, EVENT_DATE) VALUES (400, 110, 3, '2023-01-01');


-- INSURANCE
INSERT INTO INSURANCE(ID, CUSTOMER_ID, CREATION_DT, UPDATED_DT, CAR_ID, QUOTE, INSURANCE_VALUE, IS_ACTIVE) VALUES(100, 10, '2023-01-01 08:07:00.000', NULL, 100, 0.06, 4800, TRUE);
