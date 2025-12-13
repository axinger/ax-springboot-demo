package com.github.axinger.model.redis;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkStatus {

    String prefix = "plc:work";


    final String workDbListKey() {
        return StrUtil.format("{}:willWorkDbList", prefix);
    }

    private String currentWorkLineKey(int db) {
        return StrUtil.format("{}:db{}:currentWorkLine", prefix, db);
    }

    private String completedLineKey(int db) {
        return StrUtil.format("{}:db{}:finishedWorkLineList", prefix, db);
    }

    private String completedLineAllKey() {
        return StrUtil.format("{}:*:finishedWorkLineList", prefix);
    }


    final String workLineListKey(int db) {
        return StrUtil.format("{}:db{}:willWorkLineList", prefix, db);
    }


    /**
     * 允许工作 key
     *
     * @return
     */
    private String allWorkFlagKey() {
        return StrUtil.format("{}:permitWorkFlag", prefix);
    }

    private String oilPumpKey(int db) {
        return StrUtil.format("{}:db{}:oilPump", prefix, db);
    }


    public void clearAll() {

    }

    /**
     * db完成所有工作, 清除
     *
     * @param dbNo
     */
    public void clearFromWorkDbList(Integer dbNo) {
        List<Integer> list = getWorkDbList();
        List<Integer> list1 = list.stream().filter(val -> !val.equals(dbNo)).toList();

        setWorkDbList(list1);
    }

    /**
     * 正在工作的db
     *
     * @return
     */
    public List<Integer> getWorkDbList() {
        return null;
    }

    public void setWorkDbList(List<Integer> dbList) {
    }

    /**
     * 已经完成的焊缝
     *
     * @param db
     * @param line
     */
    public void setCompletedLine(int db, int line) {

    }

    public List<Integer> getCompletedLine(int db) {
        return null;
    }

    public List<Integer> getCompletedAllLine() {

        return null;
    }


    /**
     * 当前进行中的
     *
     * @param db
     * @param line
     */
    public void setCurrentLine(int db, int line) {

    }

    public void removeCurrentLine(int db) {

    }

    public Integer getCurrentLine(int db) {

        return null;
    }


    public void setWorkList(int db, List<Integer> lineList) {

    }

    public List<Integer> getWorkLine(int db) {
        return null;
    }

    public Integer getNextWorkLine(int db) {
        return null;
    }

    public Boolean getWorkFlag() {
        return null;
    }

    public void setWorkFlag(boolean status) {

    }


    public boolean isOilPump(int db) {
        return false;
    }

    public void setOilPump(int db, boolean start) {

    }

}
