package top.magicdevil.example.webapp.sample.bookstore.spark.config;

import java.io.Serializable;

public class SourceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String DBDriver;
    private String DBURL;
    private String username;
    private String password;
    private String table;

    private String userCol;
    private String itemCol;
    private String rateCol;

    public String getDBDriver() {
        return DBDriver;
    }

    public void setDBDriver(String dBDriver) {
        DBDriver = dBDriver;
    }

    public String getDBURL() {
        return DBURL;
    }

    public void setDBURL(String dBURL) {
        DBURL = dBURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getUserCol() {
        return userCol;
    }

    public void setUserCol(String userCol) {
        this.userCol = userCol;
    }

    public String getItemCol() {
        return itemCol;
    }

    public void setItemCol(String itemCol) {
        this.itemCol = itemCol;
    }

    public String getRateCol() {
        return rateCol;
    }

    public void setRateCol(String rateCol) {
        this.rateCol = rateCol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((DBDriver == null) ? 0 : DBDriver.hashCode());
        result = prime * result + ((DBURL == null) ? 0 : DBURL.hashCode());
        result = prime * result + ((itemCol == null) ? 0 : itemCol.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((rateCol == null) ? 0 : rateCol.hashCode());
        result = prime * result + ((table == null) ? 0 : table.hashCode());
        result = prime * result + ((userCol == null) ? 0 : userCol.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SourceConfig other = (SourceConfig) obj;
        if (DBDriver == null) {
            if (other.DBDriver != null)
                return false;
        } else if (!DBDriver.equals(other.DBDriver))
            return false;
        if (DBURL == null) {
            if (other.DBURL != null)
                return false;
        } else if (!DBURL.equals(other.DBURL))
            return false;
        if (itemCol == null) {
            if (other.itemCol != null)
                return false;
        } else if (!itemCol.equals(other.itemCol))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (rateCol == null) {
            if (other.rateCol != null)
                return false;
        } else if (!rateCol.equals(other.rateCol))
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        } else if (!table.equals(other.table))
            return false;
        if (userCol == null) {
            if (other.userCol != null)
                return false;
        } else if (!userCol.equals(other.userCol))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SourceConfig [DBDriver=" + DBDriver + ", DBURL=" + DBURL + ", username=" + username
                + ", password=" + password + ", table=" + table + ", userCol=" + userCol
                + ", itemCol=" + itemCol + ", rateCol=" + rateCol + "]";
    }

}
