# India-first student workspace

Semesta primarily serves Indian students without restricting nationality or deleting existing records. Programme suggestions cover school streams, UG/PG and research, engineering, commerce, professional accountancy, law, health, teaching, humanities, sciences, agriculture, hospitality, diploma/ITI and entrance preparation. The catalogue is a useful starting point, not an exhaustive directory or a claim that a course is approved. Every area accepts a custom programme and institutions remain free text.

## Academic rules

- Marks out of 100 and a 10-point scale are prominent choices; custom and legacy 5-point scales remain available. A suggested pass mark is editable and is never treated as a national rule.
- A raw marks average is not automatically SGPA/CGPA. Institutions assign grade points, credit weights and conversion rules differently. Semesta does not fabricate a percentage-to-CGPA conversion or label an unweighted marks average as CGPA.
- Assessment weights are configurable. Internal/exam 30/70 and 40/60 are optional starting examples, not regulatory prescriptions. Practicals and other assessments can use a custom split totalling 100%.
- Semester, annual, trimester and other term dates remain editable. No universal academic start date, degree duration or board timetable is imposed.
- Minimum attendance defaults to an editable 75% suggestion. Recovery and buffer calculations use recorded present/absent classes. Pending, cancelled and rescheduled classes never count as attended. Previously stored absolute absence limits remain a separate optional rule.

CBSE specifies attendance requirements for board exam eligibility; this must not be generalised to every institution or programme. [CBSE attendance circular](https://www.cbse.gov.in/cbsenew/documents/Strict_compliance_attendance_10102024.pdf). India's credit framework includes school, vocational and higher-education pathways, which motivates the broader catalogue. [UGC National Credit Framework](https://www.ugc.gov.in/pdfnews/9028476_Report-of-National-Credit-Framework.pdf).

## Money and fees

New profiles default to INR with ₹ and Indian digit grouping, including lakhs/crores. Existing explicit currency selections and stored amounts are retained. Choosing INR changes formatting; it does not perform foreign-exchange conversion. Change currency in Settings → Accessibility when migrating an existing profile.

Expenses include hostel/rent and coaching/exam preparation alongside food/mess, commuting, printing/stationery, books/supplies and other existing categories. Previously selected category sets are preserved; new categories can be enabled in the expense category controls.

Expenses → Track fees and instalments opens a personal fee ledger. Students enter actual amounts from their institution, name each instalment, record cumulative partial payments and set a due date. Outstanding balances and overdue status are derived from those records. Dates use DD/MM/YYYY; amounts use whole rupees, matching the existing expense model.

The ledger neither quotes official fees nor takes payments. Fee payments are tracked separately from daily spending; no expense is automatically duplicated. Fee reminders have their own switch and reuse notification permission, quiet hours, reminder lead time, alarm cancellation and daily rearming. Upcoming due dates use 09:00 device local time. Paid/deleted fees are removed from scheduled reminders when profile state changes.

Fee records, reminder preference and attendance target persist through DataStore and local/cloud JSON backup; ZIP export continues to include attachments. Older backups lacking these fields retain current settings. Room schema and existing stored enum names are preserved; added enums serialize by name.

## Removed developer interface

The dashboard flask was `BancoDePruebas`, a Spanish developer test harness that fabricated records and simulated UI/update states. Its overlay, ViewModel and synthetic seed actions have been deleted from production source. The separate unfinished Labs menu entry is also absent from the navigation drawer. The Notes sample-generation menu and ViewModel actions are removed; multimodal fixtures remain exclusively in JVM tests. Unused task sample generation is deleted. Student features and existing user records are preserved.

Small phones and larger text use concise Plan, Money and More navigation labels; accessibility semantics retain Schedule, Expenses and Settings. Dashboard timeline dates sit above the event title rather than inside a narrow fixed column. Assessment presets retain compatible existing assessment dates while editing weights.

Task dates, postponement confirmations and grade-save feedback use localization resources. Empty expense charts no longer draw synthetic bars; narrow timetable cells abbreviate visibly while exposing full names through accessibility semantics. Fee validation clears focus inside the dialog.
