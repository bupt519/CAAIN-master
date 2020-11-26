package org.bupt.caain.pojo.po;

public class Award {

    //    自增主键
    private int id;

    //    奖项名称
    private String award_name;

    //    是否参与投票
    private boolean voted;

    // 奖项类型 0-投一二三等奖;1-投是/否
    private int type = 0;

    public Award() {
    }

    public Award(String award_name) {
        this.award_name = award_name;
    }

    public Award(String award_name, int type) {
        this.award_name = award_name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAward_name() {
        return award_name;
    }

    public void setAward_name(String award_name) {
        this.award_name = award_name;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id=" + id +
                ", award_name='" + award_name + '\'' +
                ", voted=" + voted +
                ", type=" + type +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
