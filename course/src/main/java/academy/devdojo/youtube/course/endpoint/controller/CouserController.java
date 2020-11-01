package academy.devdojo.youtube.course.endpoint.controller;

import academy.devdojo.youtube.core.model.Course;
import academy.devdojo.youtube.course.endpoint.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("v1/admin/course")
@Api(value = "Endpoints to manager to course")
public class CouserController {

    private final CourseService courseService;

    @GetMapping
    @ApiOperation(value = "List all available courses ", response = Course[].class)
    public ResponseEntity<Iterable<Course>> list(Pageable pageable){
        return new ResponseEntity<>(courseService.list(pageable), HttpStatus.OK);
    }

}
