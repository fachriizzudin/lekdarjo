INSERT INTO subjects(id, name) VALUES(1,'Sosial dan Kependudukan');
INSERT INTO subjects(id, name) VALUES(2,'Ekonomi dan Perdagangan');
INSERT INTO subjects(id, name) VALUES(3,'Pertanian dan Pertambangan');
INSERT INTO subjects(id, name) VALUES(4,'Umum');

INSERT INTO categories(id, name, subject_fk) VALUES (1,'Agama', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (2,'Geografi', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (3,'Iklim', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (4,'Indeks Pembangunan Manusia', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (5,'Kemiskinan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (6,'Kependudukan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (7,'Kesehatan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (8,'Konsumsi dan Pengeluaran', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (9,'Pemerintahan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (10,'Pendidikan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (11,'Perumahan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (12,'Politik dan Keamanan', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (13,'Sosial Budaya', 1);
INSERT INTO categories(id, name, subject_fk) VALUES (14,'Tenaga Kerja', 1);

INSERT INTO categories(id, name, subject_fk) VALUES (15,'Energi', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (16,'Industri', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (17,'Inflasi', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (18,'Keuangan', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (19,'Komunikasi', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (20,'Konstruksi', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (21,'Pariwisata', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (22,'Produk Domestik Regional Bruto', 2);
INSERT INTO categories(id, name, subject_fk) VALUES (23,'Transportasi', 2);

INSERT INTO categories(id, name, subject_fk) VALUES (24,'Hortikultura', 3);
INSERT INTO categories(id, name, subject_fk) VALUES (25,'Perikanan', 3);
INSERT INTO categories(id, name, subject_fk) VALUES (26,'Perkebunan', 3);
INSERT INTO categories(id, name, subject_fk) VALUES (27,'Pertambangan', 3);
INSERT INTO categories(id, name, subject_fk) VALUES (28,'Peternakan', 3);
INSERT INTO categories(id, name, subject_fk) VALUES (29,'Tanaman Pangan', 3);


INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit) VALUES(1, 'Angka Morbiditas', 'Angka Morbiditas', 'Indikator Kesehatan', 'Satuan');
INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit) VALUES(2, 'Angka Harapan Hidup', 'Angka Harapan Hidup', 'Indikator Kesehatan', 'Tahun');
INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit) VALUES(3, 'PDRB Atas Dasar Harga Berlaku Menurut Lapangan Usaha', 'PDRB ADHB Menurut Lapangan Usaha', 'Indikator Kesehatan', 'Juta Rupiah');
INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit) VALUES(4, 'Indeks Pendidikan', 'Indeks Pendidikan', 'Indeks Pembangunan Manusia', 'Satuan');
INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit) VALUES(5, 'Pertumbuhan Ekonomi Menurut Lapangan Usaha', 'PDRB', 'Pertumbuhan Ekonomi Menurut Lapangan Usaha', 'Persen');

INSERT INTO roles(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO roles(id, name) VALUES(2, 'ROLE_ADMIN');






