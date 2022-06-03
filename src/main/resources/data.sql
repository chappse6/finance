INSERT INTO finance.channel (channel_id, channel_name, channel_side_revenue_share, company_side_revenue_share) VALUES (1, 'youtube', 60, 40);
INSERT INTO finance.channel (channel_id, channel_name, channel_side_revenue_share, company_side_revenue_share) VALUES (2, 'youtube1', 60, 40);

INSERT INTO finance.creator (creator_id, creator_name, revenue_share, channel_id) VALUES (1, 'testUser', 50, 1);
INSERT INTO finance.creator (creator_id, creator_name, revenue_share, channel_id) VALUES (2, 'testUser2', 50, 1);
INSERT INTO finance.creator (creator_id, creator_name, revenue_share, channel_id) VALUES (3, 'testUser1', 50, 2);
INSERT INTO finance.creator (creator_id, creator_name, revenue_share, channel_id) VALUES (4, 'testUser22', 50, 2);

INSERT INTO finance.revenue (revenue_id, channel_revenue, channel_side_revenue, company_side_revenue, date_revenue, channel_id) VALUES (1, 5000.00, 3000.00, 2000.00, '2022-06-01', 1);
INSERT INTO finance.revenue (revenue_id, channel_revenue, channel_side_revenue, company_side_revenue, date_revenue, channel_id) VALUES (2, 5000.00, 3000.00, 2000.00, '2022-06-02', 1);
INSERT INTO finance.revenue (revenue_id, channel_revenue, channel_side_revenue, company_side_revenue, date_revenue, channel_id) VALUES (3, 200000.00, 120000.00, 80000.00, '2022-07-15', 1);

INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (1, 1500.00, 1, 1);
INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (2, 1500.00, 2, 1);
INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (3, 1500.00, 1, 2);
INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (4, 1500.00, 2, 2);
INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (5, 60000.00, 1, 3);
INSERT INTO finance.revenue_creator (revenue_creator_id, creator_revenue, creator_id, revenue_id) VALUES (6, 60000.00, 2, 3);
