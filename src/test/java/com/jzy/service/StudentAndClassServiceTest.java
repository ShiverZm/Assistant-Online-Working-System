package com.jzy.service;

import com.jzy.BaseTest;
import com.jzy.config.FilePathProperties;
import com.jzy.manager.exception.InvalidFileTypeException;
import com.jzy.model.dto.StudentAndClassDetailedWithSubjectsDto;
import com.jzy.model.dto.search.StudentAndClassSearchCondition;
import com.jzy.model.excel.input.StudentListImportToDatabaseExcel;
import com.jzy.model.excel.template.SeatTableTemplateExcel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;

public class StudentAndClassServiceTest extends BaseTest {
    @Autowired
    private StudentAndClassService studentAndClassService;

    @Autowired
    private FilePathProperties filePathProperties;

    @Autowired
    protected RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private HashOperations<String, String, Object> hashOps;

    @Test
    public void insertAndUpdateStudentAndClassesFromExcel() throws Exception {
        StudentListImportToDatabaseExcel excel = new StudentListImportToDatabaseExcel("C:\\Users\\92970\\Desktop\\花名册1.xlsx");
        excel.readStudentAndClassInfoFromExcel();
        studentAndClassService.insertAndUpdateStudentAndClassesFromExcel(excel.getStudentAndClassDetailedDtos());

    }

    @Test
    public void listStudentAndClassesWithSubjectsByClassId() throws IOException, InvalidFileTypeException {
        List<StudentAndClassDetailedWithSubjectsDto> results=studentAndClassService.listStudentAndClassesWithSubjectsByClassId("U6ECFC020006");
//        AssistantTutorialExcel excel=new AssistantTutorialExcel(filePathProperties.getToolboxAssistantTutorialTemplatePathAndName("曹杨"));
        SeatTableTemplateExcel excel=new SeatTableTemplateExcel(filePathProperties.getToolboxSeatTableTemplatePathAndName("曹杨"));
        excel.writeSeatTable(results);
//        excel.submitWrite("C:\\Users\\92970\\Downloads\\1\\"+FileUtils.TEMPLATES.get(2));
        hashOps.put("h",1+"","111");
        hashOps.put("h",1+"","121");

    }

    @Test
    public void countStudentsGroupByClassGrade() {
        System.out.println(studentAndClassService.countStudentsGroupByClassGrade(new StudentAndClassSearchCondition()));
    }

    @Test
    public void countStudentsGroupByClassGradeAndType() {
        StudentAndClassSearchCondition condition=new StudentAndClassSearchCondition();
        condition.setClassYear("2019");
        condition.setClassSubSeason(null);
        condition.setClassSeason("秋下");
        System.out.println(studentAndClassService.countStudentsGroupByClassGradeAndType(condition));    }

    @Test
    public void countStudentsGroupByClassSubjectAndType() {
        StudentAndClassSearchCondition condition=new StudentAndClassSearchCondition();
        condition.setClassYear("2019");
        condition.setClassSubSeason(null);
        condition.setClassSeason("秋下");
        System.out.println(studentAndClassService.countStudentsGroupByClassSubjectAndType(condition));
    }

}