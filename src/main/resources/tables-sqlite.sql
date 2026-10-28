PRAGMA foreign_keys = ON;

-- Table structure for table `clients`
CREATE TABLE IF NOT EXISTS `transaction_request` (
     `id` INTEGER PRIMARY KEY AUTOINCREMENT,
     `existingflowid` TEXT NOT NULL UNIQUE,
     `flowid` TEXT NOT NULL UNIQUE,
     `requestJSON` TEXT NOT NULL
);
