package com

import org.springframework.dao.DataIntegrityViolationException

//https://grails101.wordpress.com/2012/04/22/test-grails-application-with-spock/
class TaskController {


        static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

        def index() {
            redirect(action: "list", params: params)
        }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 5, 100)

        def taskList = Task.createCriteria().list (params) {
            if ( params.query ) {
                ilike("description", "%${params.query}%")
            }
        }

        [taskInstanceList: taskList, taskInstanceTotal: taskList.totalCount]
    }
        def create() {
            [companyInstance: new Task(params)]
        }

        def save() {
            def companyInstance = new Task(params)
            if (!companyInstance.save(flush: true)) {
                render(view: "create", model: [companyInstance: companyInstance])
                return
            }

            flash.message = message(code: 'default.created.message', args: [message(code: 'company.label', default: 'Company'), companyInstance.id])
            redirect(action: "show", id: companyInstance.id)
        }

        def show(Long id) {
            def companyInstance = Task.get(id)
            if (!companyInstance) {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "list")
                return
            }

            [companyInstance: companyInstance]
        }

//        def showSpecficColumn() {
//            List companies = companyService.projectionsDemo()
//            println companies
//
//            [companies: companies]
//
//        }

        def edit(Long id) {
            def companyInstance = Task.get(id)
            if (!companyInstance) {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "list")
                return
            }

            [companyInstance: companyInstance]
        }

        def update(Long id, Long version) {
            def companyInstance = Company.get(id)
            if (!companyInstance) {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "list")
                return
            }

            if (version != null) {
                if (companyInstance.version > version) {
                    companyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                            [message(code: 'company.label', default: 'Company')] as Object[],
                            "Another user has updated this Company while you were editing")
                    render(view: "edit", model: [companyInstance: companyInstance])
                    return
                }
            }

            companyInstance.properties = params

            if (!companyInstance.save(flush: true)) {
                render(view: "edit", model: [companyInstance: companyInstance])
                return
            }

            flash.message = message(code: 'default.updated.message', args: [message(code: 'company.label', default: 'Company'), companyInstance.id])
            redirect(action: "show", id: companyInstance.id)
        }

        def delete(Long id) {
            def companyInstance = Company.get(id)
            if (!companyInstance) {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "list")
                return
            }

            try {
                companyInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "list")
            }
            catch (DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect(action: "show", id: id)
            }
        }
    }

