
/****** Object:  StoredProcedure [nDOCUTECH].[UPD_template_exception]    Script Date: 9/11/2019 6:11:07 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	CREATE PROC [nDOCUTECH].[UPD_template_exception]
	 @tx_action_name		varchar(32)				= NULL	OUTPUT

	, @is_active			int						= 1
	, @id_env_key			int						= NULL	OUTPUT
	, @id_user_mod_key 		int						= NULL	OUTPUT
	, @dtt_mod				datetime				= NULL	OUTPUT

	, @id_state_key			int						= NULL	OUTPUT
	, @tx_state_name		varchar(64)				= NULL	OUTPUT
	, @id_action_key		int						= NULL  OUTPUT

	, @id_event_key			int						= NULL	OUTPUT
	, @id_event_map1_key	int						= NULL	OUTPUT
	, @id_event_map2_key	int						= NULL	OUTPUT
	, @id_event_map3_key	int						= NULL	OUTPUT
	, @id_event_map4_key	int						= NULL	OUTPUT

	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL
	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL

	, @id_template_exception_key 	int 					 	= NULL 	OUTPUT
	, @id_template_exception_ver 	int 						= NULL 	OUTPUT
	, @id_template_key 				int 						= NULL 	OUTPUT
	, @tx_error_issue 				varchar(256)				= NULL
	, @tx_error_issue_source 		varchar(256)				= NULL

	, @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)				= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)			= NULL		OUTPUT
AS
BEGIN
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='UPD_template_exception', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[UPD_template_exception] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'UPD_template_exception.sp', @g_id_line_num = 147

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	EXEC @g_id_return_status = GET_environment @id_env_key = @g_id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

	SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '

	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30001 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30001
	END

	IF (((@dtt_mod IS NULL) OR (@dtt_mod = '01/01/1970')))
	BEGIN
		SELECT @dtt_mod = GETDATE()
	END

	IF (@tx_action_name != 'LOGIN')
	BEGIN
		IF (((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
		BEGIN
			RAISERROR ('Error:Int [@id_user_mod_key] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30011 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30011
		END
	END

	-- X_SET_NULL_ACTION

	SELECT  @g_tx_action_name_orig = @tx_action_name

	IF (NOT (@g_is_ext_txn = 1 OR @@trancount > 0) )
	BEGIN
		SELECT @g_is_sp_txn = 1
		BEGIN TRANSACTION
	END

		UPDATE	nDOCUTECH.T_TEMPLATE_EXCEPTION
		SET		  id_template_exception_ver				= id_template_exception_ver + 1  
				, --  id_template_exception_ver				= @id_template_exception_ver + 1
				  is_active						= ISNULL(@is_active			, is_active)
				, id_env_key					= ISNULL(@id_env_key		, id_env_key)
				, id_user_mod_key				= ISNULL(@id_user_mod_key	, id_user_mod_key)	
				, dtt_mod						= ISNULL(@dtt_mod			, dtt_mod)
				, id_event_key					= ISNULL(@id_event_key		, id_event_key)

				, id_template_key 				= ISNULL(@id_template_key 		 	, id_template_key)
				, tx_error_issue 				= ISNULL(@tx_error_issue 		 	, tx_error_issue)	
				, tx_error_issue_source 		= ISNULL(@tx_error_issue_source 	, tx_error_issue_source)	
				

		WHERE	id_template_exception_key	= @id_template_exception_key
		AND		id_template_exception_ver		= @id_template_exception_ver

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error encountered while updating -> nDOCUTECH.T_TEMPLATE_EXCEPTION ' + '@id_template_exception_key [' + CONVERT(varchar, @id_template_key) + '] '
		IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30006 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30006
		END

		SELECT @id_template_exception_ver = @id_template_exception_ver + 1

		

	IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		COMMIT TRANSACTION
		
		SELECT @tx_json_log_msg = ''
	END

	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_proc_end = GETDATE()
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_tot_elapsed  = datediff(ss, @g_dtt_proc_start, @g_dtt_proc_end)

	SELECT @g_tx_err_msg = 'Exiting[UPD_template_exception] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'UPD_template_exception.sp', @g_id_line_num = 386

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (0 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 0
END
