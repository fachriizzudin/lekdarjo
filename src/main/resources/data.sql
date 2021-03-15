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

INSERT INTO categories(id, name, subject_fk) VALUES (14,'Tenaga Kerja', 2);
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


--INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit, description) VALUES(4, 'Angka Morbiditas', 'Tahun', 'Indikator Kesehatan', 'Satuan', 'Angka Kesakitan/Morbiditas merupakan persentase penduduk yang mempunyai keluhan kesehatan. Keluhan kesehatan adalah gangguan terhadap kondisi fisik maupun jiwa, termasuk karena kecelakaan, atau hal lain yang menyebabkan terganggunya kegiatan sehari-hari. Pada umumnya keluhan kesehatan utama yang banyak dialami oleh penduduk adalah panas, sakit kepala, batuk, pilek, diare, asma/sesak nafas, sakit gigi. Orang yang menderita penyakit kronis dianggap mempunyai keluhan kesehatan walaupun pada waktu survei (satu bulan terakhir) yang bersangkutan tidak kambuh penyakitnya');
--INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit, description) VALUES(5, 'Angka Harapan Hidup', 'Tahun', 'Indikator Kesehatan', 'Satuan', 'Angka harapan hidup merupakan rata-rata tahun hidup yang masih akan dijalani oleh seseorang yang telah berhasil mencapai umur x, pada suatu tahun tertentu, dalam situasi mortalitas yang berlaku di lingkngan masyarakatnya.Angka Harapan Hidup yang terhitung untuk Indonesia dari Sensus Penduduk tahun 1971 adalah 47,7 tahun, artinya bayi-bayi yang dilahirkan menjelang tahun 1971 (periode 1967-1969) akan dapat hidup sampai 47 atau 48 tahun. Tetapi bayi-bayi yang dilahirkan menjelang tahn 1980 mempunyai usia harapan hidup lebih panjang yakni 52,2 tahun, meningkat lagi menjadi 59,8 tahun untuk bayi yang dilahirkan menjelang tahun 1990, dan bayi yang dilahirkan tahun 2000 usia harapan hidupnya mencapai 65,5 tahun. Peningkatan Angka Harapan Hidup ini menunjukkan adanya peningkatan kehidupan dan kesejahteraan bangsa Indonesia selama 30 tahun terakhir dari tahun 1970-an sampai tahun 2000');
--INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit, description) VALUES(3, 'PDRB Atas Dasar Harga Konstan Menurut Lapangan Usaha', 'Tahun', 'PDRB ADHB Menurut Lapangan Usaha', 'Juta Rupiah', 'Penjumlahan nilai tambah bruto seluruh barang dan jasa yang tercipta atau dihasilkan di wilayah domestik suatu regional/negara yang timbul akibat berbagai aktivitas ekonomi dalam suatu periode tertentu tanpa memperhatikan apakah faktor produksi yang dimiliki residen atau non-residen, di mana barang dan jasa dihitung pada harga yang tetap (harga pada tahun dasar)');
--INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit, description) VALUES(2, 'Indeks Pembangunan Manusia', 'Tahun', 'Indeks Pembangunan Manusia', 'Satuan', 'Menurut UNDP, Indeks Pembangunan Manusia (IPM) mengukur capaian pembangunan manusia berbasis sejumlah komponen dasar kualitas hidup. Sebagai ukuran kualitas hidup, IPM dibangun melalui pendekatan tiga dimensi dasar. Dimensi tersebut mencakup umur panjang dan sehat; penegtahuan, dan kehidupan yang layak. Angka IPM memberikan gambaran komprehensip mengenai tingkat pencapaian pembagunan manusia sebagai dampak dari kegiatan pembangunan yang akan dilakukan oleh suatu negara/daerah. Semakin tinggi nilai IPM suatu negara/daerah, menunjukkan pencapaian pembangunan manusianya semakin baik.');
--INSERT INTO graph_meta(id, title, horizontal, vertical, vertical_unit, description) VALUES(1, 'Pertumbuhan Ekonomi Menurut Lapangan Usaha', 'Tahun', 'Pertumbuhan Ekonomi Menurut Lapangan Usaha', 'Persen', 'Perkembangan produksi barang dan jasa di suatu wilayah perekonomian pada tahun tertentu terhadap nilai tahun sebelumnya yang dihitung berdasarkan PDB/PDRB atas dasar harga konstan');

--UPDATE graph_meta set title = 'Pertumbuhan Ekonomi Menurut Lapangan Usaha',
--horizontal = 'Tahun',
--vertical = 'PDRB',
--vertical_unit = 'Satuan',
--description = 'Perkembangan produksi barang dan jasa di suatu wilayah perekonomian pada tahun tertentu terhadap nilai tahun sebelumnya yang dihitung berdasarkan PDB/PDRB atas dasar harga konstan'
--WHERE id = 1
--
--UPDATE graph_meta set title = 'Indeks Pembangunan Manusia',
--horizontal = 'Tahun',
--vertical = 'Indeks Pembangunan Manusia',
--vertical_unit = 'Satuan',
--description = 'Menurut UNDP, Indeks Pembangunan Manusia (IPM) mengukur capaian pembangunan manusia berbasis sejumlah komponen dasar kualitas hidup. Sebagai ukuran kualitas hidup, IPM dibangun melalui pendekatan tiga dimensi dasar. Dimensi tersebut mencakup umur panjang dan sehat; penegtahuan, dan kehidupan yang layak. Angka IPM memberikan gambaran komprehensip mengenai tingkat pencapaian pembagunan manusia sebagai dampak dari kegiatan pembangunan yang akan dilakukan oleh suatu negara/daerah. Semakin tinggi nilai IPM suatu negara/daerah, menunjukkan pencapaian pembangunan manusianya semakin baik'
--WHERE id = 2
--
--UPDATE graph_meta set title = 'PDRB Atas Dasar Harga Konstan Menurut Lapangan Usaha',
--horizontal = 'Tahun',
--vertical = 'PDRB ADHB Menurut Lapangan Usaha',
--vertical_unit = 'Jutaan Rupiah',
--description = 'Penjumlahan nilai tambah bruto seluruh barang dan jasa yang tercipta atau dihasilkan di wilayah domestik suatu regional/negara yang timbul akibat berbagai aktivitas ekonomi dalam suatu periode tertentu tanpa memperhatikan apakah faktor produksi yang dimiliki residen atau non-residen, di mana barang dan jasa dihitung pada harga yang tetap (harga pada tahun dasar)'
--WHERE id = 2
--
--UPDATE graph_meta set title = 'Angka Morbiditas',
--horizontal = 'Tahun',
--vertical = 'Angka Morbiditas',
--vertical_unit = 'Satuan',
--description = 'Angka Kesakitan/Morbiditas merupakan persentase penduduk yang mempunyai keluhan kesehatan. Keluhan kesehatan adalah gangguan terhadap kondisi fisik maupun jiwa, termasuk karena kecelakaan, atau hal lain yang menyebabkan terganggunya kegiatan sehari-hari. Pada umumnya keluhan kesehatan utama yang banyak dialami oleh penduduk adalah panas, sakit kepala, batuk, pilek, diare, asma/sesak nafas, sakit gigi. Orang yang menderita penyakit kronis dianggap mempunyai keluhan kesehatan walaupun pada waktu survei (satu bulan terakhir) yang bersangkutan tidak kambuh penyakitnya'
--WHERE id = 4
--
--UPDATE graph_meta set title = 'Angka Harapan Hidup',
--horizontal = 'Tahun',
--vertical = 'Angka Harapan Hidup',
--vertical_unit = 'Satuan',
--description = 'Angka harapan hidup merupakan rata-rata tahun hidup yang masih akan dijalani oleh seseorang yang telah berhasil mencapai umur x, pada suatu tahun tertentu, dalam situasi mortalitas yang berlaku di lingkngan masyarakatnya.Angka Harapan Hidup yang terhitung untuk Indonesia dari Sensus Penduduk tahun 1971 adalah 47,7 tahun, artinya bayi-bayi yang dilahirkan menjelang tahun 1971 (periode 1967-1969) akan dapat hidup sampai 47 atau 48 tahun. Tetapi bayi-bayi yang dilahirkan menjelang tahn 1980 mempunyai usia harapan hidup lebih panjang yakni 52,2 tahun, meningkat lagi menjadi 59,8 tahun untuk bayi yang dilahirkan menjelang tahun 1990, dan bayi yang dilahirkan tahun 2000 usia harapan hidupnya mencapai 65,5 tahun. Peningkatan Angka Harapan Hidup ini menunjukkan adanya peningkatan kehidupan dan kesejahteraan bangsa Indonesia selama 30 tahun terakhir dari tahun 1970-an sampai tahun 2000'
--WHERE id = 5





INSERT INTO roles(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO roles(id, name) VALUES(2, 'ROLE_ADMIN');






