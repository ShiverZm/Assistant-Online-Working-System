package com.jzy.model.excel.template;

import com.jzy.manager.constant.ExcelConstants;
import com.jzy.manager.exception.ClassTooManyStudentsException;
import com.jzy.manager.exception.ExcelColumnNotFoundException;
import com.jzy.manager.exception.InvalidFileTypeException;
import com.jzy.model.dto.StudentAndClassDetailedWithSubjectsDto;
import com.jzy.model.excel.AbstractTemplateExcel;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.IOException;
import java.util.List;

/**
 * @author JinZhiyun
 * @version 1.0
 * @ClassName AssistantTutorialTemplate
 * @description 助教工作手册模板的模型类
 * @date 2019/11/1 15:28
 **/
public class AssistantTutorialExcel extends AbstractTemplateExcel {
    private static final long serialVersionUID = 2416400649170324596L;

    private static final String CAMPUS_COLUMN = ExcelConstants.CAMPUS_COLUMN_2;
    private static final String CLASS_ID_COLUMN = ExcelConstants.CLASS_ID_COLUMN_3;
    private static final String TEACHER_NAME_COLUMN = ExcelConstants.TEACHER_NAME_COLUMN_3;
    private static final String ASSISTANT_NAME_COLUMN = ExcelConstants.ASSISTANT_NAME_COLUMN_3;
    private static final String STUDENT_ID_COLUMN = ExcelConstants.STUDENT_ID_COLUMN_2;
    private static final String STUDENT_NAME_COLUMN = ExcelConstants.STUDENT_NAME_COLUMN_2;
    private static final String STUDENT_PHONE_COLUMN = ExcelConstants.STUDENT_PHONE_COLUMN_2;
    private static final String IS_OLD_STUDENT = ExcelConstants.IS_OLD_STUDENT;
    private static final String TEACHER_REQUIREMENT_COLUMN = ExcelConstants.TEACHER_REQUIREMENT_COLUMN;
    private static final String STUDENT_SCHOOL_COLUMN = ExcelConstants.STUDENT_SCHOOL_COLUMN_2;
    private static final String SUBJECTS_COLUMN = ExcelConstants.SUBJECTS_COLUMN;

    /**
     * 班上默认最大人数上限
     */
    private static final int MAX_CLASS_STUDENTS_COUNT = 100;

    /**
     * 开班电话表sheet索引
     */
    private static final int CLASS_START_SHEET_INDEX = 1;

    /**
     * 签到表sheet索引
     */
    private static final int SIGN_SHEET_INDEX = 0;

    /**
     * 信息回访表sheet索引
     */
    private static final int CALLBACK_SHEET_INDEX = 2;

    /**
     * 座位表sheet索引
     */
    private static final int SEAT_SHEET_INDEX = 6;

    public AssistantTutorialExcel(String inputFile) throws IOException, InvalidFileTypeException {
        super(inputFile);
    }

    /**
     * 根据学生在助教班上出现次数决定使用什么颜色填充单元格背景，返回对应颜色的index值
     *
     * @param count 学生在助教班上出现次数
     * @return 对应颜色的index值
     */
    private short getBackGroundColorIndexByStudentOccurCount(int count){
        if (count == 2){
            //出现2次，绿色
            return HSSFColor.LIGHT_GREEN.index;
        }
        if (count == 3){
            //出现3次，蓝色
            return HSSFColor.LIGHT_BLUE.index;
        }
        if (count == 4){
            //出现4次，粉色
            return HSSFColor.ROSE.index;
        }
        if (count >= 5){
            //出现5次以上，红色
            return HSSFColor.RED.index;
        }

        return -1;
    }

    /**
     * 修改制作开班电话表
     *
     * @param data 从数据库中读取到的信息或手动输入的表格中读到的信息，以及用户输入的信息
     * @return 写入成功与否
     * @throws IOException                   写excel的io异常
     * @throws ClassTooManyStudentsException 班级的学生人数过多，不能写入模板表格。
     *                                       这里由于模板中只给了100条空行用来放置要写入的数据。因此如果入参行数超过这个阈值会抛出此异常
     * @throws ExcelColumnNotFoundException  列属性中有未匹配的属性名
     */
    public boolean writeClassStartSheet(List<StudentAndClassDetailedWithSubjectsDto> data) throws IOException, ClassTooManyStudentsException, ExcelColumnNotFoundException {
        // 获得班上学生总人数
        int rowCountToSave = data.size();
        if (rowCountToSave > MAX_CLASS_STUDENTS_COUNT) {
            throw new ClassTooManyStudentsException("班上学生人数超过了" + MAX_CLASS_STUDENTS_COUNT + "！");
        }

        int startRow = 0;
        // 先扫描第startRow行找到"校区"、"班号"、"教师姓名"等信息所在列的位置
        int columnIndexOfCampus = -1, columnIndexOfClassId = -2, columnIndexOfTeacherName = -3, columnIndexOfAssistantName = -4, columnIndexOfStudentId = -7, columnIndexOfStudentName = -9, columnIndexOfStudentPhone = -10, columnIndexOfIsOldStudent = -10, columnIndexOfTeacherRequirement = -11, columnIndexOfStudentSchool = -11, columnIndexOfSubjects = -12;
        int row0ColumnCount = getColumnCount(CLASS_START_SHEET_INDEX, startRow); // 第startRow行的列数
        for (int i = 0; i < row0ColumnCount; i++) {
            String value = getValueAt(CLASS_START_SHEET_INDEX, startRow, i);
            if (value != null) {
                switch (value) {
                    case CAMPUS_COLUMN:
                        columnIndexOfCampus = i;
                        break;
                    case CLASS_ID_COLUMN:
                        columnIndexOfClassId = i;
                        break;
                    case TEACHER_NAME_COLUMN:
                        columnIndexOfTeacherName = i;
                        break;
                    case ASSISTANT_NAME_COLUMN:
                        columnIndexOfAssistantName = i;
                        break;
                    case STUDENT_ID_COLUMN:
                        columnIndexOfStudentId = i;
                        break;
                    case STUDENT_NAME_COLUMN:
                        columnIndexOfStudentName = i;
                        break;
                    case STUDENT_PHONE_COLUMN:
                        columnIndexOfStudentPhone = i;
                        break;
                    case IS_OLD_STUDENT:
                        columnIndexOfIsOldStudent = i;
                        break;
                    case TEACHER_REQUIREMENT_COLUMN:
                        columnIndexOfTeacherRequirement = i;
                        break;
                    case STUDENT_SCHOOL_COLUMN:
                        columnIndexOfStudentSchool = i;
                        break;
                    case SUBJECTS_COLUMN:
                        columnIndexOfSubjects = i;
                        break;
                    default:
                }
            }
        }

        if (columnIndexOfCampus < 0 || columnIndexOfClassId < 0 || columnIndexOfTeacherName < 0 || columnIndexOfAssistantName < 0
                || columnIndexOfStudentId < 0 || columnIndexOfStudentName < 0 || columnIndexOfStudentPhone < 0
                || columnIndexOfTeacherRequirement < 0 || columnIndexOfStudentSchool < 0) {
//                || columnIndexOfSubjects < 0) {
            //列属性中有未匹配的属性名
            throw new ExcelColumnNotFoundException("助教工作手册-开班电话表sheet列属性中有未匹配的属性名");
        }


        for (int i = startRow; i < rowCountToSave + startRow; i++) {
            StudentAndClassDetailedWithSubjectsDto object = data.get(i - startRow);
            //遍历每行要填的学生上课信息对象
            // 填校区
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfCampus, object.getClassCampus());
            // 填班号
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfClassId, object.getClassId());
            // 填教师姓名
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfTeacherName, object.getTeacherName());
            // 填助教
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfAssistantName, object.getAssistantName());
            // 填学员编号
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfStudentId, object.getStudentId());
            // 填学员姓名
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfStudentName, object.getStudentName());
            // 姓名背景色
            int count=object.getCountOfSpecifiedAssistant();
            if (count > 1) {
                //如果同一助教半夏出现次数大于1才改填充色
                updateCellBackgroundColor(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfStudentName, getBackGroundColorIndexByStudentOccurCount(object.getCountOfSpecifiedAssistant()));
            }
            // 填学员联系方式
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfStudentPhone, object.getStudentPhone());
            // 填类别，是否老生
            String isOld = object.isOldStudent() ? "老生" : "新生";
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfIsOldStudent, isOld);
            // 填任课教师要求
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfTeacherRequirement, object.getClassTeacherRequirement());
            // 填学校
            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfStudentSchool, object.getStudentSchool());
            // 填所有在读学科
//            String subjectsToString = object.getSubjects() == null ? "" : object.getSubjects().toString();
//            setValueAt(CLASS_START_SHEET_INDEX, i + 1, columnIndexOfSubjects, subjectsToString);
        }

        // 删除多余行
        removeRows(CLASS_START_SHEET_INDEX, rowCountToSave + 4, MAX_CLASS_STUDENTS_COUNT);

        return true;
    }

    /**
     * 修改制作签到表
     *
     * @param data 从数据库中读取到的信息或手动输入的表格中读到的信息，以及用户输入的信息
     * @return 写入成功与否
     * @throws IOException                   写excel的io异常
     * @throws ClassTooManyStudentsException 班级的学生人数过多，不能写入模板表格。
     *                                       这里由于模板中只给了100条空行用来放置要写入的数据。因此如果入参行数超过这个阈值会抛出此异常
     * @throws ExcelColumnNotFoundException  列属性中有未匹配的属性名
     */
    public boolean writeSignSheet(List<StudentAndClassDetailedWithSubjectsDto> data) throws IOException, ClassTooManyStudentsException, ExcelColumnNotFoundException {
        // 获得班上学生总人数
        int rowCountToSave = data.size();
        if (rowCountToSave > MAX_CLASS_STUDENTS_COUNT) {
            throw new ClassTooManyStudentsException("班上学生人数超过了" + MAX_CLASS_STUDENTS_COUNT + "！");
        }

        StudentAndClassDetailedWithSubjectsDto dto = new StudentAndClassDetailedWithSubjectsDto();
        if (rowCountToSave > 0) {
            //取第一个对象为例，填充表格第一行、第二行
            dto = data.get(0);
        }
        // 填表格第一行
        String str1 = "班号：" + dto.getClassId() + "                  班级名称：" + dto.getClassName();
        setValueAt(SIGN_SHEET_INDEX, 0, 0, str1);
        // 填表格第二行
        String str2 = "上课时间：" + dto.getClassSimplifiedTime() + "         上课教室：" + dto.getClassroom() +
                "          教师：" + dto.getTeacherName() + "             助教：" + dto.getAssistantName();
        setValueAt(SIGN_SHEET_INDEX, 1, 0, str2);

        int startRow = 2;
        // 先扫描第startRow行找到"学员编号"、"学员姓名"、"家长联系方式"等信息所在列的位置
        int columnIndexOfStudentId = -7, columnIndexOfStudentName = -9, columnIndexOfStudentPhone = -10;
        int row0ColumnCount = getColumnCount(SIGN_SHEET_INDEX, startRow); // 第startRow行的列数
        for (int i = 0; i < row0ColumnCount; i++) {
            String value = getValueAt(SIGN_SHEET_INDEX, startRow, i);
            if (value != null) {
                switch (value) {
                    case STUDENT_ID_COLUMN:
                        columnIndexOfStudentId = i;
                        break;
                    case STUDENT_NAME_COLUMN:
                        columnIndexOfStudentName = i;
                        break;
                    case STUDENT_PHONE_COLUMN:
                        columnIndexOfStudentPhone = i;
                        break;
                    default:
                }
            }
        }

        if (columnIndexOfStudentId < 0 || columnIndexOfStudentName < 0 || columnIndexOfStudentPhone < 0) {
            //列属性中有未匹配的属性名
            throw new ExcelColumnNotFoundException("助教工作手册-签到表sheet列属性中有未匹配的属性名");
        }


        for (int i = startRow; i < rowCountToSave + startRow; i++) {
            StudentAndClassDetailedWithSubjectsDto object = data.get(i - startRow);
            //遍历每行要填的学生上课信息对象
            // 填学员编号
            setValueAt(SIGN_SHEET_INDEX, i + 1, columnIndexOfStudentId, object.getStudentId());
            // 填学员姓名
            setValueAt(SIGN_SHEET_INDEX, i + 1, columnIndexOfStudentName, object.getStudentName());
            // 填学员联系方式
            setValueAt(SIGN_SHEET_INDEX, i + 1, columnIndexOfStudentPhone, object.getStudentPhone());
            // 填任课教师要求
        }

        // 删除多余行
        removeRows(SIGN_SHEET_INDEX, rowCountToSave + 4 + startRow + 1,
                MAX_CLASS_STUDENTS_COUNT + startRow + 1);

        //根据上课次数删除多余列
        //TODO

        return true;
    }

    /**
     * 修改制作信息回访表
     *
     * @param data 从数据库中读取到的信息或手动输入的表格中读到的信息，以及用户输入的信息
     * @return 写入成功与否
     * @throws IOException                   写excel的io异常
     * @throws ClassTooManyStudentsException 班级的学生人数过多，不能写入模板表格。
     *                                       这里由于模板中只给了100条空行用来放置要写入的数据。因此如果入参行数超过这个阈值会抛出此异常
     * @throws ExcelColumnNotFoundException  列属性中有未匹配的属性名
     */
    public boolean writeCallbackSheet(List<StudentAndClassDetailedWithSubjectsDto> data) throws IOException, ClassTooManyStudentsException, ExcelColumnNotFoundException {
        // 获得班上学生总人数
        int rowCountToSave = data.size();
        if (rowCountToSave > MAX_CLASS_STUDENTS_COUNT) {
            throw new ClassTooManyStudentsException("班上学生人数超过了" + MAX_CLASS_STUDENTS_COUNT + "！");
        }

        int startRow = 0;
        // 先扫描第startRow行找到"校区"、"班号"、"教师姓名"等信息所在列的位置
        int columnIndexOfCampus = -1, columnIndexOfClassId = -2, columnIndexOfTeacherName = -3, columnIndexOfAssistantName = -4, columnIndexOfStudentId = -7, columnIndexOfStudentName = -9, columnIndexOfIsOldStudent = -10;
        int row0ColumnCount = getColumnCount(CALLBACK_SHEET_INDEX, startRow); // 第startRow行的列数
        for (int i = 0; i < row0ColumnCount; i++) {
            String value = getValueAt(CALLBACK_SHEET_INDEX, startRow, i);
            if (value != null) {
                switch (value) {
                    case CAMPUS_COLUMN:
                        columnIndexOfCampus = i;
                        break;
                    case CLASS_ID_COLUMN:
                        columnIndexOfClassId = i;
                        break;
                    case TEACHER_NAME_COLUMN:
                        columnIndexOfTeacherName = i;
                        break;
                    case ASSISTANT_NAME_COLUMN:
                        columnIndexOfAssistantName = i;
                        break;
                    case STUDENT_ID_COLUMN:
                        columnIndexOfStudentId = i;
                        break;
                    case STUDENT_NAME_COLUMN:
                        columnIndexOfStudentName = i;
                        break;
                    case IS_OLD_STUDENT:
                        columnIndexOfIsOldStudent = i;
                        break;
                    default:
                }
            }
        }

        if (columnIndexOfCampus < 0 || columnIndexOfClassId < 0 || columnIndexOfTeacherName < 0 || columnIndexOfAssistantName < 0
                || columnIndexOfStudentId < 0 || columnIndexOfStudentName < 0) {
            //列属性中有未匹配的属性名
            throw new ExcelColumnNotFoundException("助教工作手册-首课回访表sheet列属性中有未匹配的属性名");
        }


        for (int i = startRow; i < rowCountToSave + startRow; i++) {
            StudentAndClassDetailedWithSubjectsDto object = data.get(i - startRow);
            //遍历每行要填的学生上课信息对象
            // 填校区
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfCampus, object.getClassCampus());
            // 填班号
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfClassId, object.getClassId());
            // 填教师姓名
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfTeacherName, object.getTeacherName());
            // 填助教
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfAssistantName, object.getAssistantName());
            // 填学员编号
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfStudentId, object.getStudentId());
            // 填学员姓名
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfStudentName, object.getStudentName());
            // 填类别，是否老生
            String isOld = object.isOldStudent() ? "老生" : "新生";
            setValueAt(CALLBACK_SHEET_INDEX, i + 1, columnIndexOfIsOldStudent, isOld);
        }

        // 删除多余行
        removeRows(CALLBACK_SHEET_INDEX, rowCountToSave + 4, MAX_CLASS_STUDENTS_COUNT);

        return true;
    }

    /**
     * 修改制作座位表，这里暂时不做处理标准，
     * 若之后有更改要另外操作请使用 {@link SeatTableTemplateExcel}
     *
     * @param data 从花名册中读取到的信息以及用户输入的信息
     * @return 写入成功与否
     */
    public boolean setSeatSheet(List<StudentAndClassDetailedWithSubjectsDto> data) {
        // TODO
        return true;
    }

    /**
     * 使用巴啦啦能量！完成对助教工作手册的所有处理（不含开班电话）！
     *
     * @param data 从花名册或数据库中读取到的信息以及用户输入的信息
     * @return 写入成功与否
     * @throws IOException                   写excel的io异常
     * @throws ClassTooManyStudentsException 班级的学生人数过多，不能写入模板表格。
     *                                       这里由于模板中只给了100条空行用来放置要写入的数据。因此如果入参行数超过这个阈值会抛出此异常
     * @throws ExcelColumnNotFoundException  列属性中有未匹配的属性名
     */
    public boolean writeAssistantTutorialWithoutSeatTable(List<StudentAndClassDetailedWithSubjectsDto> data) throws IOException, ExcelColumnNotFoundException, ClassTooManyStudentsException {
        return writeClassStartSheet(data) && writeSignSheet(data) && writeCallbackSheet(data);
    }
}
